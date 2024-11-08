package store.dto;

import java.util.Map;
import store.model.Promotion;

public record ProductBuilder(String name, int price, Promotion promotion) {
    public static ProductBuilder createWithoutPromotion(ProductFields productFields) {
        return new ProductBuilder(
                productFields.name(),
                productFields.price(),
                null
        );
    }

    public static ProductBuilder createWithPromotion(
            ProductFields promotionFields,
            Map<String, Promotion> promotions
    ) {
        return new ProductBuilder(
                promotionFields.name(),
                promotionFields.price(),
                promotions.get(promotionFields.promotion())
        );
    }
}
