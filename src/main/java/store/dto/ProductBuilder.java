package store.dto;

import java.util.List;
import java.util.Map;
import store.model.Promotion;

public class ProductBuilder {
    private static final int NAME_COLUMN = 0;
    private static final int PRICE_COLUMN = 1;
    private static final int STOCK_COUNT_COLUMN = 2;
    private static final int PROMOTION_COLUMN = 3;

    private final String name;
    private final int price;
    private final int stockCount;

    protected ProductBuilder(String name, int price, int stockCount) {
        this.name = name;
        this.price = price;
        this.stockCount = stockCount;
    }

    public static ProductBuilder of(List<String> fields, Map<String, Promotion> promotions) {
        String name = fields.get(NAME_COLUMN);
        int price = Integer.parseInt(fields.get(PRICE_COLUMN));
        int stockCount = Integer.parseInt(fields.get(STOCK_COUNT_COLUMN));
        Promotion promotion = promotions.get(fields.get(PROMOTION_COLUMN));

        if (promotion == null) {
            return new ProductBuilder(name, price, stockCount);
        }
        return new PromotionProductBuilder(name, price, stockCount, promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStockCount() {
        return stockCount;
    }
}
