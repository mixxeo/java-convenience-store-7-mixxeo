package store.model;

import store.dto.PromotionProductBuilder;

public class PromotionProduct extends Product {
    private final Promotion promotion;

    public PromotionProduct(PromotionProductBuilder builder) {
        super(builder);
        this.promotion = builder.getPromotion();
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
