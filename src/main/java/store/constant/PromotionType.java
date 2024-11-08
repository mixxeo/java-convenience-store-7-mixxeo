package store.constant;

import java.util.Arrays;
import store.model.Quantity;

public enum PromotionType {
    BUY_ONE_GET_ONE_FREE(1),
    BUY_TWO_GET_ONE_FREE(2);

    private final int buyQuantity;

    PromotionType(int buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public static PromotionType getType(int buyQuantity) {
        return Arrays.stream(PromotionType.values())
                .filter(type -> type.buyQuantity == buyQuantity)
                .findFirst()
                .orElse(null);
    }

    public boolean isEligibleQuantity(Quantity quantity) {
        return quantity.value() % buyQuantity == buyQuantity - 1;
    }
}
