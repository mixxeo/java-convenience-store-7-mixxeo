package store.controller;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constant.ExceptionMessage;
import store.dto.ProductInformation;
import store.model.Order;
import store.model.Products;
import store.model.Quantity;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private static final Pattern ORDER_ITEM_PATTERN = Pattern.compile("^\\[(.+)-(\\d+)]$");
    private static final int PRODUCT_NAME_GROUP_INDEX = 1;
    private static final int QUANTITY_GROUP_INDEX = 2;

    private final Products products;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(Products products, InputView inputView, OutputView outputView) {
        this.products = products;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        displayProductCatalog();
        Order order = requestWithRetry(this::requestOrder);
    }

    private void displayProductCatalog() {
        List<ProductInformation> productInformation = products.products().stream()
                .map(ProductInformation::from)
                .toList();
        outputView.printProductCatalog(productInformation);
    }

    private Order requestOrder() {
        outputView.printRequestOrder();
        String orderInput = inputView.read();
        List<String> items = parseOrder(orderInput);
        return createOrder(items);
    }

    private List<String> parseOrder(String orderInput) {
        return List.of(orderInput.split(","));
    }

    private Order createOrder(List<String> items) {
        Order order = new Order();
        for(String item:items) {
            addOrderItem(item, order);
        }
        return order;
    }

    private void addOrderItem(String item, Order order) {
        Matcher matcher = ORDER_ITEM_PATTERN.matcher(item);
        validateOrderFormat(matcher);
        String productName = extractProductName(matcher);
        Quantity quantity = extractQuantity(matcher);
        order.addItem(productName, quantity);
    }

    private void validateOrderFormat(Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
        }
    }

    private String extractProductName(Matcher matcher) {
        String productName = matcher.group(PRODUCT_NAME_GROUP_INDEX);
        validateIsExistingProduct(productName);
        return productName;
    }

    private void validateIsExistingProduct(String name) {
        if (products.findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    private Quantity extractQuantity(Matcher matcher) {
        String quantityInput = matcher.group(QUANTITY_GROUP_INDEX);
        return Quantity.from(quantityInput);
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
