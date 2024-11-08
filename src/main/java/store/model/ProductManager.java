package store.model;

import java.time.LocalDate;
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

    public void validateProductsInStock() {
        boolean allProductsOutOfStock = products.stream().allMatch(stockManager::isOutOfStock);
        if (allProductsOutOfStock) {
            throw new IllegalStateException();
        }
    }

    public void validateHasProduct(String name) {
        if (findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    public void validateStockAvailability(Product product, Quantity quantity, LocalDate date) {
        if (stockManager.isInSufficientStock(product, quantity, date)) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_MORE_THAN_STOCK_COUNT.getMessage());
        }
    }

    public Promotion getInProgressPromotionOfProduct(Product product, LocalDate date) {
        if (product.hasInProgressPromotion(date)) {
            return product.getPromotion();
        }
        return null;
    }

    public Product findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
