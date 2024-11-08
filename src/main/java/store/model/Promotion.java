package store.model;

import java.time.LocalDate;
import store.dto.PromotionBuilder;

public record Promotion(String name, int buyQuantity, LocalDate startDate, LocalDate endDate) {
    public Promotion(PromotionBuilder builder) {
        this(builder.name(), builder.buyQuantity(), builder.startDate(), builder.endDate());
    }

    public boolean isInProgress(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
