package store.model;

import java.util.List;
import store.constant.ExceptionMessage;

public record Order(List<OrderItem> items) {
    public Order {
        validateHasDuplicated(items);
    }

    private void validateHasDuplicated(List<OrderItem> items) {
        long distinctNamesCount = items.stream()
                .map(OrderItem::getProductName)
                .distinct()
                .count();
        if (items.size() != distinctNamesCount) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }

    public List<OrderItem> findEligibleItemsForPromotion() {
        return items.stream()
                .filter(OrderItem::isEligibleForPromotion)
                .toList();
    }

    public List<OrderItem> getHasPromotionItems() {
        return items.stream()
                .filter(OrderItem::hasPromotion)
                .toList();
    }
}
