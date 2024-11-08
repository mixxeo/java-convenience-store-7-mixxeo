package store.model;

import java.util.List;
import store.constant.ExceptionMessage;

public class ProductManager {
    private final List<Product> products;
    private final StockManager stockManager;

    public ProductManager(List<Product> products, StockManager stockManager) {
        this.products = products;
        this.stockManager = stockManager;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public StockManager getStockManager() {
        return this.stockManager;
    }

    public void validateHasProduct(String name) {
        if (findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    public Product findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
