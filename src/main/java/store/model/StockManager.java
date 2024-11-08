package store.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StockManager {
    private final Map<Product, Integer> stock = new HashMap<>();
    private final Map<Product, Integer> promotionStock = new HashMap<>();

    public void addStock(Product product, int stockCount, int promotionStockCount) {
        stock.put(product, stockCount);
        promotionStock.put(product, promotionStockCount);
    }

    public boolean isInSufficientStock(Product product, Quantity quantity, LocalDate date) {
        int totalStockCount = getStock(product);
        if (product.hasInProgressPromotion(date)) {
            totalStockCount += getPromotionStock(product);
        }
        return quantity.value() > totalStockCount;
    }

    public boolean isOutOfStock(Product product) {
        return getStock(product) == 0 && getPromotionStock(product) == 0;
    }

    public int getStock(Product product) {
        return stock.get(product);
    }

    public int getPromotionStock(Product product) {
        return promotionStock.get(product);
    }
}
