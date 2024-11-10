package store.model;

import java.time.LocalDate;
import store.dto.PromotionBuilder;

public record Promotion(String name, PromotionType type, LocalDate startDate, LocalDate endDate) {
    public Promotion(final PromotionBuilder builder) {
        this(builder.name(), builder.type(), builder.startDate(), builder.endDate());
    }

    public boolean isInProgress(final LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isEligibleQuantity(final Quantity quantity) {
        return type.isEligibleQuantity(quantity);
    }

    public int getMaxAppliedCount(final int count) {
        return type.calculateMaxAppliedCount(count);
    }

    public int getFreeQuantity(final int appliedQuantity) {
        return type.calculateFreeQuantity(appliedQuantity);
    }
}
