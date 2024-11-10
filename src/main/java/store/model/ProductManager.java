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

    public void validateHasProduct(final String name) {
        if (findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    public void validateStockAvailability(final Product product, final Quantity quantity, final LocalDate date) {
        if (stockManager.isInSufficientStock(product, quantity, date)) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_MORE_THAN_STOCK_COUNT.getMessage());
        }
    }

    public Promotion getInProgressPromotionOfProduct(final Product product, final LocalDate date) {
        if (product.hasInProgressPromotion(date)) {
            return product.getPromotion();
        }
        return null;
    }

    public Product findByName(final String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public int getInSufficientPromotionStock(final Product product, final Quantity quantity) {
        return stockManager.calculateInSufficientPromotionStock(product, quantity);
    }

    public int getPromotionAppliedQuantity(final Product product, final Quantity quantity) {
        return stockManager.calculatePromotionAppliedQuantity(product, quantity);
    }

    public void deductStock(Order order) {
        stockManager.deductStock(order);
    }
}
