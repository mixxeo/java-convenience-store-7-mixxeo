package store.dto;

import java.util.List;
import java.util.Map;
import store.model.Promotion;

public record ProductBuilder(String name, int price, int stockCount, Promotion promotion, int promotionStockCount) {
    public static ProductBuilder of(List<ProductFields> productFields, Map<String, Promotion> promotions) {
        ProductFields normalFields = findProductFieldsByPromotionStatus(productFields, false);
        ProductFields promotionFields = findProductFieldsByPromotionStatus(productFields, true);

        if (promotionFields == null) {
            return createWithoutPromotion(normalFields);
        }
        return createWithPromotion(normalFields, promotionFields, promotions);
    }

    private static ProductFields findProductFieldsByPromotionStatus(
            List<ProductFields> productFields,
            boolean hasPromotion
    ) {
        return productFields.stream()
                .filter(fields -> fields.hasPromotion() == hasPromotion)
                .findFirst()
                .orElse(null);
    }

    private static ProductBuilder createWithoutPromotion(ProductFields productFields) {
        return new ProductBuilder(
                productFields.name(),
                productFields.price(),
                productFields.stockCount(),
                null,
                0
        );
    }

    private static ProductBuilder createWithPromotion(
            ProductFields normalFields,
            ProductFields promotionFields,
            Map<String, Promotion> promotions
    ) {
        return new ProductBuilder(
                promotionFields.name(),
                promotionFields.price(),
                selectStockCount(normalFields, promotionFields),
                promotions.get(promotionFields.promotion()),
                promotionFields.stockCount()
        );
    }

    private static int selectStockCount(ProductFields normalFields, ProductFields promotionFields) {
        if (normalFields != null) {
            return normalFields.stockCount();
        }
        return 0;
    }
}
