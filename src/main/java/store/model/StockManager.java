package store.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import store.constant.ExceptionMessage;

public class StockManager {
    private final Map<Product, Integer> stock = new HashMap<>();
    private final Map<Product, Integer> promotionStock = new HashMap<>();

    public void addStock(Product product, int stockCount, int promotionStockCount) {
        stock.put(product, stockCount);
        promotionStock.put(product, promotionStockCount);
    }

    public void validateIsStockSufficient(Product product, Quantity quantity, LocalDateTime now) {
        int totalStockCount = getStock(product);
        if (product.hasInProgressPromotion(now)) {
            totalStockCount += getPromotionStock(product);
        }

        if (quantity.value() > totalStockCount) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_MORE_THAN_STOCK_COUNT.getMessage());
        }
    }

    public int getStock(Product product) {
        return stock.get(product);
    }

    public int getPromotionStock(Product product) {
        return promotionStock.get(product);
    }
}
