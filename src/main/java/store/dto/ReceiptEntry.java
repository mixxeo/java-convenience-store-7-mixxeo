package store.dto;

import store.model.OrderItem;

public record ReceiptEntry(
        String productName,
        int quantity,
        int price,
        int freeQuantity
) {
    public static ReceiptEntry from(final OrderItem orderItem) {
        return new ReceiptEntry(
                orderItem.getProductName(),
                orderItem.getQuantity().value(),
                orderItem.calculatePrice(),
                orderItem.getFreeQuantity().value()
        );
    }
}
