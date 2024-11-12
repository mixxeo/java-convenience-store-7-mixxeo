package store.model;

import java.util.Arrays;

public enum PromotionType {
    BUY_ONE_GET_ONE_FREE(1),
    BUY_TWO_GET_ONE_FREE(2);

    private static final int freeQuantity = 1;

    private final int buyQuantity;

    PromotionType(final int buyQuantity) {
        this.buyQuantity = buyQuantity;
    }

    public static PromotionType getType(final int buyQuantity) {
        return Arrays.stream(PromotionType.values())
                .filter(type -> type.buyQuantity == buyQuantity)
                .findFirst()
                .orElse(null);
    }

    public boolean isEligibleQuantity(final Quantity quantity) {
        return (quantity.value() + 1) % (buyQuantity + freeQuantity) == 0;
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
