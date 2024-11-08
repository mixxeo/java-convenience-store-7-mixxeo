package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constant.ExceptionMessage;
import store.dto.CatalogEntry;
import store.model.Order;
import store.model.OrderItem;
import store.model.Product;
import store.model.ProductManager;
import store.model.Quantity;
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
        List<CatalogEntry> catalogEntry = productManager.getProducts().stream()
                .flatMap(product -> convertToCatalogEntries(product, productManager).stream())
                .toList();
        outputView.printProductCatalog(catalogEntry);
    }

    private List<CatalogEntry> convertToCatalogEntries(Product product, ProductManager productManager) {
        List<CatalogEntry> catalogEntries = new ArrayList<>();
        if (product.hasPromotion()) {
            catalogEntries.add(CatalogEntry.ofPromotion(product, productManager.getPromotionStock(product)));
        }
        catalogEntries.add(CatalogEntry.of(product, productManager.getStock(product)));
        return catalogEntries;
    }


    private Order requestOrder(ProductManager productManager) {
        outputView.printRequestOrder();
        String orderInput = inputView.read();
        List<String> items = List.of(orderInput.split(","));
        return createOrder(items, productManager);
    }

    private Order createOrder(List<String> items, ProductManager productManager) {
        List<OrderItem> orderItems = items.stream()
                .map(item -> createOrderItem(item, productManager))
                .toList();
        return new Order(orderItems);
    }

    private OrderItem createOrderItem(String item, ProductManager productManager) {
        Matcher matcher = ORDER_ITEM_PATTERN.matcher(item);
        validateOrderFormat(matcher);

        String productName = matcher.group(PRODUCT_NAME_GROUP_INDEX);
        Quantity quantity = Quantity.from(matcher.group(QUANTITY_GROUP_INDEX));
        return OrderItem.of(productName, quantity, productManager);
    }

    private void validateOrderFormat(Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
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
