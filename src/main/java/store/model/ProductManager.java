package store.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
        return new ArrayList<>(this.products);
    }

    public int getNormalStock(final Product product) {
        return stockManager.getNormalStock(product);
    }

    public int getPromotionStock(final Product product) {
        return stockManager.getPromotionStock(product);
    }

    public void validateProductsInStock() {
        boolean allProductsOutOfStock = products.stream().allMatch(stockManager::isOutOfStock);
        if (allProductsOutOfStock) {
            throw new IllegalStateException(ExceptionMessage.ALL_PRODUCTS_OUT_OF_STOCK.getMessage());
        }
    }

    public void validateStock(final String name, final Quantity quantity, LocalDate date) {
        validateHasProduct(name);
        Product product = findByName(name);
        validateStockAvailability(product, quantity, date);
    }

    private void validateHasProduct(final String name) {
        if (findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    public Product findByName(final String name) {
        return products.stream()
                .filter(product -> product.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void validateStockAvailability(final Product product, final Quantity quantity, final LocalDate date) {
        if (stockManager.isInsufficientStock(product, quantity, date)) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_MORE_THAN_STOCK_COUNT.getMessage());
        }
    }

    public int getInsufficientPromotionStock(final Product product, final Quantity quantity) {
        return stockManager.calculateInsufficientPromotionStock(product, quantity);
    }

    public int getPromotionFreeQuantity(final Product product, final Quantity quantity) {
        int promotionAppliedQuantity = stockManager.calculatePromotionAppliedQuantity(product, quantity);
        return product.promotion().getFreeQuantity(promotionAppliedQuantity);
    }

    public void deductStock(Order order) {
        stockManager.deductStock(order);
    }
}
