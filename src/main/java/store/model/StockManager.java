package store.model;

import java.util.HashMap;
import java.util.Map;

public class StockManager {
    private final Map<Product, Integer> stock = new HashMap<>();
    private final Map<Product, Integer> promotionStock = new HashMap<>();

    public void addStock(Product product, int stockCount, int promotionStockCount) {
        stock.put(product, stockCount);
        promotionStock.put(product, promotionStockCount);
    }

    public int getStock(Product product) {
        return stock.get(product);
    }

    public int getPromotionStock(Product product) {
        return promotionStock.get(product);
    }
}
