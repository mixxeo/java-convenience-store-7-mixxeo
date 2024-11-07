package store;

import store.controller.ProductController;
import store.model.Products;

public class Application {
    public static void main(String[] args) {
        ProductController productController = new ProductController();
        Products products = productController.initialize();
    }
}
