package store.dto;

import java.util.List;
import store.model.Order;

public record Receipt(
        List<ReceiptEntry> entries,
        int totalQuantity,
        String totalPrice,
        String promotionDiscount,
        String memberShipDiscount,
        String paidAmount
) {
    public static Receipt of(List<ReceiptEntry> entries, Order order, boolean isMembership) {
        int totalQuantity = order.getTotalQuantity();
        int totalPrice = order.getTotalPrice();
        int promotionDiscount = order.calculatePromotionDiscount();
        int membershipDiscount = order.calculateMembershipDiscount(isMembership);
        int paidAmount = totalPrice - promotionDiscount - membershipDiscount;
        return new Receipt(entries, totalQuantity, String.format("%,d", totalPrice),
                String.format("-%,d", promotionDiscount), String.format("-%,d", membershipDiscount),
                String.format("%,d", paidAmount));
    }
}