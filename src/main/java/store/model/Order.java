package store.model;

import java.util.List;
import store.constant.ExceptionMessage;

public class Order {
    private final List<OrderItem> items;

    public Order(List<OrderItem> items) {
        validateHasDuplicated(items);
        this.items = items;
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
}
