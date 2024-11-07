package store.model;

import java.time.LocalDate;
import store.dto.PromotionBuilder;

public record Promotion(String name, int buyQuantity, LocalDate startDate, LocalDate endDat) {
    public static Promotion from(PromotionBuilder builder) {
        return new Promotion(builder.name(), builder.buyQuantity(), builder.startDate(), builder.endDate());
    }
}
