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
    private static final int PROMOTION_BENEFIT_QUANTITY = 1;

    private final ProductService productService;
    private final OrderService orderService;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(
            final ProductService productService,
            final OrderService orderService,
            final InputView inputView,
            final OutputView outputView
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

    private void processOrder(final ProductManager productManager) {
        displayProductCatalog(productManager);

        Order order = requestOrder(productManager);
        processPromotions(order, productManager);
        boolean isMembership = confirmMembershipDiscount();

        displayReceipt(order, isMembership);
        productManager.deductStock(order);
        suggestReorder(productManager);
    }

    private void displayProductCatalog(final ProductManager productManager) {
        List<CatalogEntry> catalogEntries = productService.convertToCatalogEntries(productManager);
        outputView.printProductCatalog(catalogEntries);
    }

    private Order requestOrder(final ProductManager productManager) {
        productManager.validateProductsInStock();
        outputView.printRequestOrder();
        return requestWithRetry(() -> {
            String orderInput = inputView.read();
            return orderService.createOrder(orderInput, productManager);
        });
    }

    private void processPromotions(final Order order, final ProductManager productManager) {
        informPromotionBenefits(order.getEligibleItemsForPromotion());
        applyPromotions(order.getPromotionItems(), productManager);
    }

    private void informPromotionBenefits(List<OrderItem> eligibleOrderItemsForPromotion) {
        for (OrderItem orderItem : eligibleOrderItemsForPromotion) {
            ResponseType response = suggestPromotionBenefit(orderItem);
            if (response.equals(ResponseType.YES)) {
                orderItem.increaseQuantity(PROMOTION_BENEFIT_QUANTITY);
            }
        }
    }

    private void applyPromotions(List<OrderItem> promotionOrderItems, ProductManager productManager) {
        for (OrderItem orderItem : promotionOrderItems) {
            Product product = orderItem.getProduct();
            confirmPromotionStock(product, orderItem, productManager);
            orderService.applyPromotionFreeQuantity(product, orderItem, productManager);
        }
    }

    private void confirmPromotionStock(Product product, OrderItem orderItem, ProductManager productManager) {
        int inSufficientStock = productManager.getInSufficientPromotionStock(product, orderItem.getQuantity());
        if (inSufficientStock > 0) {
            ResponseType response = notifyFullPriceQuantity(product.getName(), inSufficientStock);
            if (response.equals(ResponseType.NO)) {
                orderItem.decreaseQuantity(inSufficientStock);
            }
        }
    }

    private ResponseType suggestPromotionBenefit(final OrderItem orderItem) {
        outputView.printSuggestPromotionBenefit(orderItem.getProductName());
        return getYesOrNoResponse();
    }

    private ResponseType notifyFullPriceQuantity(final String productName, final int quantity) {
        outputView.printFullPriceQuantityNotification(productName, quantity);
        return getYesOrNoResponse();
    }

    private boolean confirmMembershipDiscount() {
        outputView.printConfirmMembershipDiscount();
        ResponseType response = getYesOrNoResponse();
        return response.equals(ResponseType.YES);
    }

    private void displayReceipt(final Order order, final boolean isMembership) {
        Receipt receipt = orderService.generateReceipt(order, isMembership);
        outputView.printReceipt(receipt);
    }

    private void suggestReorder(final ProductManager productManager) {
        outputView.printSuggestReorder();
        ResponseType response = getYesOrNoResponse();
        if (response.equals(ResponseType.YES)) {
            processOrder(productManager);
        }
    }

    private ResponseType getYesOrNoResponse() {
        return requestWithRetry(() -> {
            String response = inputView.read();
            return ResponseType.fromString(response);
        });
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
