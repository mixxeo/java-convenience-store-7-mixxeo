package store.model;

import java.time.LocalDate;
import store.dto.ProductBuilder;

public record Product(String name, int price, Promotion promotion) {
    public Product(final ProductBuilder builder) {
        this(builder.name(), builder.price(), builder.promotion());
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public boolean hasInProgressPromotion(final LocalDate now) {
        if (!hasPromotion()) {
            return false;
        }
        return promotion.isInProgress(now);
    }
}
