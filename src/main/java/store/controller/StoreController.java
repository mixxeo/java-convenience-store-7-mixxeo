package store.controller;

import java.util.List;
import store.constant.ExceptionMessage;
import store.dto.ProductInformation;
import store.model.Products;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {
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
        requestOrder();
    }

    private void displayProductCatalog() {
        List<ProductInformation> productInformation = products.products().stream()
                .map(ProductInformation::from)
                .toList();
        outputView.printProductCatalog(productInformation);
    }

    private void requestOrder() {
        outputView.printRequestOrder();
        String orderInput = inputView.read();
        parseOrder(orderInput);
    }

    private void parseOrder(String orderInput) {
        List<String> items = List.of(orderInput.split(","));
        items.forEach(this::validateOrderFormat);
    }

    private void validateOrderFormat(String item) {
        if (!item.matches("^\\[.+-\\d+]$")) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
        }
    }
}
