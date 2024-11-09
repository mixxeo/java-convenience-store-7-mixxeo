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

    public int calculateInSufficientPromotionStock(Product product, Quantity quantity) {
        int maxAvailablePromotionStock = getMaxAvailablePromotionStock(product);
        return Integer.max(quantity.value() - maxAvailablePromotionStock, 0);
    }

    public int calculatePromotionAppliedQuantity(Product product, Quantity quantity) {
        Promotion promotion = product.getPromotion();
        int maxAvailablePromotionStock = getMaxAvailablePromotionStock(product);
        int maxPromotionAppliedQuantity = promotion.getMaxAppliedCount(quantity.value());
        return Integer.min(maxAvailablePromotionStock, maxPromotionAppliedQuantity);
    }

    private int getMaxAvailablePromotionStock(Product product) {
        Promotion promotion = product.getPromotion();
        if (promotion == null) {
            return 0;
        }
        return promotion.getMaxAppliedCount(getPromotionStock(product));
    }

    public int getStock(Product product) {
        return stock.get(product);
    }

    public int getPromotionStock(Product product) {
        return promotionStock.get(product);
    }

    public void deductStock(Order order) {
        for (OrderItem orderItem:order.items()) {
            Product product = orderItem.getProduct();
            int remainingQuantity = orderItem.getQuantity().value();

            remainingQuantity = deductPromotionStock(product, remainingQuantity, orderItem);
            deductNormalStock(product, remainingQuantity);
        }
    }

    private int deductPromotionStock(Product product, int quantity, OrderItem orderItem) {
        if (!orderItem.hasPromotion()) {
            return quantity;
        }

        int currentPromotionStock = promotionStock.getOrDefault(product, 0);
        int deductedPromotionStock = Integer.min(currentPromotionStock, quantity);
        promotionStock.put(product, currentPromotionStock - deductedPromotionStock);
        return quantity - deductedPromotionStock;
    }

    private void deductNormalStock(Product product, int quantity) {
        if (quantity <= 0) {
            return;
        }

        int currentStock = stock.getOrDefault(product, 0);
        stock.put(product, currentStock - quantity);
    }
}
