package store.model;

import java.util.List;

public record Promotions(List<Promotion> promotions) {
    public Promotion findByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.name().equals(name))
                .findFirst()
                .orElse(null);
    }
}
