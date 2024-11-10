package store.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class StockManager {
    private final Map<Product, Integer> normalStock = new HashMap<>();
    private final Map<Product, Integer> promotionStock = new HashMap<>();

    public void addStock(final Product product, final int normalStockCount, final int promotionStockCount) {
        normalStock.put(product, normalStockCount);
        promotionStock.put(product, promotionStockCount);
    }

    public boolean isInSufficientStock(final Product product, final Quantity quantity, final LocalDate date) {
        int totalStockCount = getNormalStock(product);
        if (product.hasInProgressPromotion(date)) {
            totalStockCount += getPromotionStock(product);
        }
        return quantity.value() > totalStockCount;
    }

    public boolean isOutOfStock(final Product product) {
        return getNormalStock(product) == 0 && getPromotionStock(product) == 0;
    }

    public int calculateInSufficientPromotionStock(final Product product, final Quantity quantity) {
        int maxAvailablePromotionStock = getMaxAvailablePromotionStock(product);
        return Integer.max(quantity.value() - maxAvailablePromotionStock, 0);
    }

    public int calculatePromotionAppliedQuantity(final Product product, final Quantity quantity) {
        Promotion promotion = product.getPromotion();
        int maxAvailablePromotionStock = getMaxAvailablePromotionStock(product);
        int maxPromotionAppliedQuantity = promotion.getMaxAppliedCount(quantity.value());
        return Integer.min(maxAvailablePromotionStock, maxPromotionAppliedQuantity);
    }

    private int getMaxAvailablePromotionStock(final Product product) {
        Promotion promotion = product.getPromotion();
        if (promotion == null) {
            return 0;
        }
        return promotion.getMaxAppliedCount(getPromotionStock(product));
    }

    public int getNormalStock(final Product product) {
        return normalStock.get(product);
    }

    public int getPromotionStock(final Product product) {
        return promotionStock.get(product);
    }

    public void deductStock(final Order order) {
        for (OrderItem orderItem:order.items()) {
            Product product = orderItem.getProduct();
            int remainingQuantity = orderItem.getQuantity().value();

            remainingQuantity = deductPromotionStock(product, remainingQuantity, orderItem);
            deductNormalStock(product, remainingQuantity);
        }
    }

    private int deductPromotionStock(final Product product, final int quantity, final OrderItem orderItem) {
        if (!orderItem.hasPromotion()) {
            return quantity;
        }

        int currentPromotionStock = promotionStock.getOrDefault(product, 0);
        int deductedPromotionStock = Integer.min(currentPromotionStock, quantity);
        promotionStock.put(product, currentPromotionStock - deductedPromotionStock);
        return quantity - deductedPromotionStock;
    }

    private void deductNormalStock(final Product product, final int quantity) {
        if (quantity <= 0) {
            return;
        }

        int currentStock = normalStock.getOrDefault(product, 0);
        normalStock.put(product, currentStock - quantity);
    }
}
