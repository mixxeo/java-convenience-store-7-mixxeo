package store.model;

import java.util.Arrays;

public enum PromotionType {
    BUY_ONE_GET_ONE_FREE(1, 1),
    BUY_TWO_GET_ONE_FREE(2, 1);

    private final int buyQuantity;
    private final int freeQuantity;

    PromotionType(final int buyQuantity, final int freeQuantity) {
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
    }

    public static PromotionType getType(final int buyQuantity, final int freeQuantity) {
        return Arrays.stream(PromotionType.values())
                .filter(type -> type.buyQuantity == buyQuantity && type.freeQuantity == freeQuantity)
                .findFirst()
                .orElse(null);
    }

    public boolean isEligibleQuantity(final Quantity quantity) {
        return (quantity.value() + freeQuantity) % (buyQuantity + freeQuantity) == 0;
    }

    public int calculateMaxAppliedCount(final int count) {
        int totalQuantity = buyQuantity + freeQuantity;
        return count - (count % totalQuantity);
    }

    public int calculateFreeQuantity(final int appliedQuantity) {
        int totalQuantity = buyQuantity + freeQuantity;
        return appliedQuantity / totalQuantity;
    }
}
