package store.dto;

import java.util.List;
import store.model.Promotion;
import store.model.Promotions;

public class ProductBuilder {
    private static final int NAME_COLUMN = 0;
    private static final int PRICE_COLUMN = 1;
    private static final int QUANTITY_COLUMN = 2;
    private static final int PROMOTION_COLUMN = 3;

    private final String name;
    private final int price;
    private final int quantity;

    protected ProductBuilder(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static ProductBuilder of(List<String> fields, Promotions promotions) {
        String name = fields.get(NAME_COLUMN);
        int price = Integer.parseInt(fields.get(PRICE_COLUMN));
        int quantity = Integer.parseInt(fields.get(QUANTITY_COLUMN));
        Promotion promotion = promotions.findByName(fields.get(PROMOTION_COLUMN));

        if (promotion == null) {
            return new ProductBuilder(name, price, quantity);
        }
        return new PromotionProductBuilder(name, price, quantity, promotion);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
