package store.controller;

import java.util.List;
import store.dto.CatalogEntry;
import store.model.Order;
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
        displayProductCatalog(productManager);
        Order order = requestWithRetry(() -> requestOrder(productManager));
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
