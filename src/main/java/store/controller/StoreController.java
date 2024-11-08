package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constant.ExceptionMessage;
import store.dto.ProductInformation;
import store.model.Order;
import store.model.Product;
import store.model.ProductManager;
import store.model.Quantity;
import store.model.StockManager;
import store.service.ProductService;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
    private static final Pattern ORDER_ITEM_PATTERN = Pattern.compile("^\\[(.+)-(\\d+)]$");
    private static final int PRODUCT_NAME_GROUP_INDEX = 1;
    private static final int QUANTITY_GROUP_INDEX = 2;

    private final ProductService productService;
    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(ProductService productService, InputView inputView, OutputView outputView) {
        this.productService = productService;
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        ProductManager productManager = productService.createProductManager();
        displayProductCatalog(productManager);
        Order order = requestWithRetry(() -> requestOrder(productManager));
    }

    private void displayProductCatalog(ProductManager productManager) {
        List<ProductInformation> productInformation = productManager.getProducts().stream()
                .flatMap(product -> convertToProductInformation(product, productManager).stream())
                .toList();
        outputView.printProductCatalog(productInformation);
    }

    private List<ProductInformation> convertToProductInformation(Product product, ProductManager productManager) {
        List<ProductInformation> productInformation = new ArrayList<>();
        StockManager stockManager = productManager.getStockManager();
        if (product.hasPromotion()) {
            productInformation.add(ProductInformation.ofPromotion(product, stockManager.getPromotionStock(product)));
        }
        productInformation.add(ProductInformation.of(product, stockManager.getPromotionStock(product)));
        return productInformation;
    }


    private Order requestOrder(ProductManager productManager) {
        outputView.printRequestOrder();
        String orderInput = inputView.read();
        List<String> items = List.of(orderInput.split(","));
        return createOrder(items, productManager);
    }

    private Order createOrder(List<String> items, ProductManager productManager) {
        Order order = new Order();
        for (String item : items) {
            processOrderItem(item, order, productManager);
        }
        return order;
    }

    private void processOrderItem(String item, Order order, ProductManager productManager) {
        Matcher matcher = ORDER_ITEM_PATTERN.matcher(item);
        validateOrderFormat(matcher);

        String productName = extractProductName(matcher, productManager);
        Quantity quantity = extractQuantity(matcher);

        order.addItem(productName, quantity);
    }

    private void validateOrderFormat(Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
        }
    }

    private String extractProductName(Matcher matcher, ProductManager productManager) {
        String productName = matcher.group(PRODUCT_NAME_GROUP_INDEX);
        productManager.validateHasProduct(productName);
        return productName;
    }

    private Quantity extractQuantity(Matcher matcher) {
        return Quantity.from(matcher.group(QUANTITY_GROUP_INDEX));
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
