package store;

import store.service.OrderService;
import store.service.ProductService;
import store.controller.StoreController;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        StoreController storeController = new StoreController(productService, orderService, inputView, outputView);
        storeController.run();
    }
}
