package store.dto;

import store.model.Promotion;

public class PromotionProductBuilder extends ProductBuilder {
    private final Promotion promotion;

    public PromotionProductBuilder(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
