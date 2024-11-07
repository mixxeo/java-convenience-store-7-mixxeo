package store.controller;

import java.util.List;
import store.dto.ProductInformation;
import store.model.Products;
import store.view.OutputView;

public class StoreController {
    private final Products products;
    public final OutputView outputView;

    public StoreController(Products products, OutputView outputView) {
        this.products = products;
        this.outputView = outputView;
    }

    public void run() {
        displayProductCatalog();
    }

    private void displayProductCatalog() {
        List<ProductInformation> productInformation = products.products().stream()
                .map(ProductInformation::from)
                .toList();
        outputView.printProductCatalog(productInformation);
    }
}
