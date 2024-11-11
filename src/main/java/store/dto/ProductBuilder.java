package store.dto;

import java.util.Map;
import store.model.Promotion;

public record ProductBuilder(String name, int price, Promotion promotion) {
    public static ProductBuilder createWithoutPromotion(final ProductFields productFields) {
        return new ProductBuilder(
                productFields.name(),
                productFields.price(),
                null
        );
    }

    public static ProductBuilder createWithPromotion(
            final ProductFields productFields,
            final Map<String, Promotion> promotions
    ) {
        return new ProductBuilder(
                productFields.name(),
                productFields.price(),
                promotions.get(productFields.promotion())
        );
    }
}
