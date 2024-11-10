package store.dto;

import java.util.List;
import store.model.Order;

public record Receipt(
        List<ReceiptEntry> entries,
        int totalQuantity,
        int totalPrice,
        int promotionDiscount,
        int memberShipDiscount
) {
    public static Receipt of(List<ReceiptEntry> entries, Order order, boolean isMembership) {
        return new Receipt(
                entries,
                order.calculateTotalQuantity(),
                order.calculateTotalPrice(),
                order.calculatePromotionDiscount(),
                order.calculateMembershipDiscount(isMembership)
        );
    }

    public int getPaidAmount() {
        return totalPrice - promotionDiscount - memberShipDiscount;
    }
}