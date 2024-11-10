package store.controller;

import java.util.List;
import store.constant.ResponseType;
import store.dto.CatalogEntry;
import store.dto.Receipt;
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
        boolean isMembership = suggestApplyingMembershipDiscount();
        generateReceipt(order, isMembership);
        productManager.deductStock(order);
        suggestReorder(productManager);
    }

    private void displayProductCatalog(ProductManager productManager) {
        List<CatalogEntry> catalogEntries = productService.convertToCatalogEntries(productManager);
        outputView.printProductCatalog(catalogEntries);
    }

    private Order requestOrder(ProductManager productManager) {
        outputView.printRequestOrder();
        String orderInput = inputView.read();
        return orderService.createOrder(orderInput, productManager);
    }

    private void applyPromotions(Order order, ProductManager productManager) {
        List<OrderItem> eligibleOrderItemsForPromotion = order.findEligibleItemsForPromotion();
        for (OrderItem orderItem : eligibleOrderItemsForPromotion) {
            ResponseType response = requestWithRetry(() -> suggestAddingQuantityForPromotion(orderItem));
            if (response.equals(ResponseType.YES)) {
                orderItem.increaseQuantity();
            }
        }

        List<OrderItem> hasPromotionOrderItems = order.getHasPromotionItems();
        for (OrderItem orderItem : hasPromotionOrderItems) {
            Product product = productManager.findByName(orderItem.getProductName());
            int inSufficientStock = productManager.getInSufficientPromotionStock(product, orderItem.getQuantity());
            if (inSufficientStock > 0) {
                ResponseType response = requestWithRetry(() -> notifyFullPriceQuantity(product.getName(), inSufficientStock));
                if (response.equals(ResponseType.NO)) {
                    orderItem.decreaseQuantity(inSufficientStock);
                }
            }

            int promotionAppliedQuantity = productManager.getPromotionAppliedQuantity(product, orderItem.getQuantity());
            orderItem.setFreeQuantity(promotionAppliedQuantity);
        }
    }

    private ResponseType suggestAddingQuantityForPromotion(OrderItem orderItem) {
        outputView.printOfferFreeProduct(orderItem.getProductName());
        return getYesOrNoResponse();
    }

    private ResponseType notifyFullPriceQuantity(String productName, int quantity) {
        outputView.printFullPriceQuantityNotification(productName, quantity);
        return getYesOrNoResponse();
    }

    private boolean suggestApplyingMembershipDiscount() {
        outputView.printSuggestApplyingMembershipDiscount();
        ResponseType response = requestWithRetry(this::getYesOrNoResponse);
        return response.equals(ResponseType.YES);
    }

    private void suggestReorder(ProductManager productManager) {
        outputView.printSuggestReorder();
        ResponseType response = requestWithRetry(this::getYesOrNoResponse);
        if (response.equals(ResponseType.YES)) {
            processOrder(productManager);
        }
    }

    private ResponseType getYesOrNoResponse() {
        String response = inputView.read();
        return ResponseType.fromString(response);
    }

    private void generateReceipt(Order order, boolean isMembership) {
        Receipt receipt = orderService.generateReceipt(order, isMembership);
        outputView.printReceipt(receipt);
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
