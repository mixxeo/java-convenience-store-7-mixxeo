package store;

import store.service.ProductService;
import store.controller.StoreController;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        StoreController storeController = new StoreController(productService, inputView, outputView);
        storeController.run();
    }
}
