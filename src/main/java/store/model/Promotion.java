package store.model;

import java.time.LocalDate;
import store.dto.PromotionBuilder;

public record Promotion(String name, PromotionType type, LocalDate startDate, LocalDate endDate) {
    public Promotion(PromotionBuilder builder) {
        this(builder.name(), builder.type(), builder.startDate(), builder.endDate());
    }

    public boolean isInProgress(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isEligibleQuantity(Quantity quantity) {
        return type.isEligibleQuantity(quantity);
    }

    public int getMaxAvailableStock(int currentStock) {
        return type.calculateMaxAvailableStock(currentStock);
    }
}
