package store.dto;

import java.util.List;

public record ProductFields(String name, int price, int stockCount, String promotion) {
    private static final int NAME_COLUMN = 0;
    private static final int PRICE_COLUMN = 1;
    private static final int STOCK_COUNT_COLUMN = 2;
    private static final int PROMOTION_COLUMN = 3;

    public static ProductFields from(final List<String> fields) {
        String name = fields.get(NAME_COLUMN);
        int price = Integer.parseInt(fields.get(PRICE_COLUMN));
        int stockCount = Integer.parseInt(fields.get(STOCK_COUNT_COLUMN));
        String promotion = fields.get(PROMOTION_COLUMN);

        return new ProductFields(name, price, stockCount, promotion);
    }

    public boolean hasPromotion() {
        return !promotion.equals("null");
    }
}
