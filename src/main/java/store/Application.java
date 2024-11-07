package store;

import store.controller.ProductController;
import store.controller.StoreController;
import store.model.Products;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        ProductController productController = new ProductController();
        Products products = productController.initialize();

        OutputView outputView = new OutputView();
        StoreController storeController = new StoreController(products, outputView);
        storeController.run();
    }
}
