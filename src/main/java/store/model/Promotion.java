package store.model;

import java.time.LocalDateTime;
import store.dto.PromotionBuilder;

public record Promotion(String name, int buyQuantity, LocalDateTime startDate, LocalDateTime endDat) {
    public static Promotion from(PromotionBuilder builder) {
        return new Promotion(builder.name(), builder.buyQuantity(), builder.startDate(), builder.endDate());
    }
}
