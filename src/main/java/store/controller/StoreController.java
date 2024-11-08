package store.controller;

import java.util.List;
import store.constant.ExceptionMessage;
import store.dto.CatalogEntry;
import store.model.Order;
import store.model.OrderItem;
import store.model.Product;
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
        processOrder(productManager);
    }

    private void processOrder(ProductManager productManager) {
        productManager.validateProductsInStock();
        displayProductCatalog(productManager);
        Order order = requestWithRetry(() -> requestOrder(productManager));
        applyPromotions(order, productManager);
        applyMembershipSale(order);
        suggestReorder(productManager);
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

    private void applyPromotions(Order order, ProductManager productManager) {
        List<OrderItem> eligibleOrderItemsForPromotion = order.findEligibleItemsForPromotion();
        for (OrderItem orderItem : eligibleOrderItemsForPromotion) {
            String response = requestWithRetry(() -> suggestAddingQuantityForPromotion(orderItem));
            if (response.equals("Y")) {
                orderItem.increaseQuantity();
            }
        }

        List<OrderItem> hasPromotionOrderItems = order.getHasPromotionItems();
        for (OrderItem orderItem : hasPromotionOrderItems) {
            Product product = productManager.findByName(orderItem.getProductName());
            int inSufficientStock = productManager.getInSufficientPromotionStock(product, orderItem.getQuantity());
            if (inSufficientStock > 0) {
                String response = requestWithRetry(() -> notifyFullPriceQuantity(product.getName(), inSufficientStock));
                if (response.equals("N")) {
                    orderItem.decreaseQuantity(inSufficientStock);
                }
            }

            int promotionAppliedQuantity = productManager.getPromotionAppliedQuantity(product, orderItem.getQuantity());
            orderItem.setFreeQuantity(promotionAppliedQuantity);
        }
    }

    private String suggestAddingQuantityForPromotion(OrderItem orderItem) {
        outputView.printOfferFreeProduct(orderItem.getProductName());
        return getYesOrNotResponse();
    }

    private String notifyFullPriceQuantity(String productName, int quantity) {
        outputView.printFullPriceQuantityNotification(productName, quantity);
        return getYesOrNotResponse();
    }

    private void applyMembershipSale(Order order) {
        String response = requestWithRetry(this::suggestApplyingMemberShipSale);
        if (response.equals("Y")) {
            order.applyMembershipSale();
        }
    }

    private String suggestApplyingMemberShipSale() {
        outputView.printSuggestMembershipSale();
        return getYesOrNotResponse();
    }

    private void suggestReorder(ProductManager productManager) {
        String response = requestWithRetry(() -> {
            outputView.printSuggestReorder();
            return getYesOrNotResponse();
        });
        if (response.equals("Y")) {
            processOrder(productManager);
        }
    }

    private String getYesOrNotResponse() {
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
