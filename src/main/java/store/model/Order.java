package store.model;

import java.util.List;
import store.constant.ExceptionMessage;

public record Order(List<OrderItem> items) {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

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

    public int getTotalQuantity() {
        List<Integer> quantities = items.stream()
                .map(item -> item.getQuantity().value())
                .toList();
        return sum(quantities);
    }

    public int getTotalPrice() {
        List<Integer> prices = items.stream()
                .map(OrderItem::calculatePrice)
                .toList();
        return sum(prices);
    }

    public int calculatePromotionDiscount() {
        List<Integer> promotionPrices = items.stream()
                .map(OrderItem::calculateFreePrice)
                .toList();
        return sum(promotionPrices);
    }

    public int calculateMembershipDiscount(boolean isMembership) {
        if (!isMembership) {
            return 0;
        }
        List<OrderItem> notPromotionAppliedItems = getNotAppliedPromotionItems();
        List<Integer> notPromotionItemPrices = notPromotionAppliedItems.stream()
                .map(OrderItem::calculatePrice)
                .toList();
        int totalNotPromotionPrice = sum(notPromotionItemPrices);
        int discount = (int) Math.floor(totalNotPromotionPrice * MEMBERSHIP_DISCOUNT_RATE);
        return Integer.min(discount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private int sum(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public List<OrderItem> getNotAppliedPromotionItems() {
        return items.stream()
                .filter(item -> item.getFreeQuantity().value() == 0)
                .toList();
    }
}
