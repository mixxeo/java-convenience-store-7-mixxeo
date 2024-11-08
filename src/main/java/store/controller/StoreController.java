package store.controller;

import java.util.List;
import store.constant.ExceptionMessage;
import store.dto.CatalogEntry;
import store.model.Order;
import store.model.OrderItem;
import store.model.ProductManager;
import store.service.OrderService;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private final ProductService productService;
    private final OrderService orderService;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(
            ProductService productService,
            OrderService orderService,
            InputView inputView,
            OutputView outputView
    ) {
        this.productService = productService;
        this.orderService = orderService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        ProductManager productManager = productService.createProductManager();
        productManager.validateProductsInStock();
        displayProductCatalog(productManager);
        Order order = requestWithRetry(() -> requestOrder(productManager));
        applyPromotions(order);
    }

    private void displayProductCatalog(ProductManager productManager) {
        List<CatalogEntry> catalogEntry = productManager.getProducts().stream()
                .flatMap(product -> productService.convertToCatalogEntries(product, productManager).stream())
                .toList();
        outputView.printProductCatalog(catalogEntry);
    }

    private Order requestOrder(ProductManager productManager) {
        outputView.printRequestOrder();
        String orderInput = inputView.read();
        List<String> items = List.of(orderInput.split(","));
        return orderService.createOrder(items, productManager);
    }

    private void applyPromotions(Order order) {
        List<OrderItem> orderItems = order.findEligibleOrderItemsForPromotion();
        for (OrderItem orderItem:orderItems) {
            String response = requestWithRetry(() -> suggestAddingQuantityForPromotion(orderItem));
            if (response.equals("Y")) {
                orderItem.increaseQuantity();
            }
        }
    }

    private String suggestAddingQuantityForPromotion(OrderItem orderItem) {
        outputView.printOfferFreeProduct(orderItem.getProductName());
        String response = inputView.read();
        validateResponse(response);
        return response;
    }

    private void validateResponse(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }

    private <T> T requestWithRetry(SupplierWithException<T> request) {
        try {
            return request.get();
        } catch (IllegalArgumentException e) {
            outputView.printMessage(e.getMessage());
            return requestWithRetry(request);
        }
    }

    @FunctionalInterface
    private interface SupplierWithException<T> {
        T get() throws IllegalArgumentException;
    }
}
