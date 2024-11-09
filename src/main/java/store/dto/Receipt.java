package store.dto;

import java.util.List;
import store.model.Order;

public record Receipt(
        List<ReceiptEntry> entries,
        int totalQuantity,
        int totalPrice,
        int promotionDiscount,
        int memberShipDiscount,
        int paidAmount
) {
    public static Receipt of(List<ReceiptEntry> entries, Order order, boolean isMembership) {
        int totalQuantity = order.getTotalQuantity();
        int totalPrice = order.getTotalPrice();
        int promotionDiscount = order.calculatePromotionDiscount();
        int membershipDiscount = order.calculateMembershipDiscount(isMembership);
        int paidAmount = totalPrice + promotionDiscount + membershipDiscount;
        return new Receipt(entries, totalQuantity, totalPrice, promotionDiscount, membershipDiscount, paidAmount);
    }
}