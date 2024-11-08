package store.model;

import java.time.LocalDateTime;
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

    public int getStock(Product product) {
        return stockManager.getStock(product);
    }

    public int getPromotionStock(Product product) {
        return stockManager.getPromotionStock(product);
    }

    public void validateHasProduct(String name) {
        if (findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    public void validateStockAvailability(Product product, Quantity quantity, LocalDateTime now) {
        if (stockManager.isInSufficientStock(product, quantity.value(), now.toLocalDate())) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_MORE_THAN_STOCK_COUNT.getMessage());
        }
    }

    public Product findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
