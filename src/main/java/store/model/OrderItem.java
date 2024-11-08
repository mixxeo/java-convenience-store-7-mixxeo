package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class OrderItem {
    private final Product product;
    private Quantity quantity;
    private final Promotion promotion;
    private Quantity freeQuantity;

    private OrderItem(Product product, Quantity quantity, Promotion promotion) {
        this.product = product;
        this.quantity = quantity;
        this.promotion = promotion;
        this.freeQuantity = Quantity.createInitialQuantity();
    }

    public static OrderItem of(String productName, Quantity quantity, ProductManager productManager) {
        LocalDate now = DateTimes.now().toLocalDate();

        productManager.validateHasProduct(productName);
        Product product = productManager.findByName(productName);
        productManager.validateStockAvailability(product, quantity, now);
        Promotion promotion = productManager.getInProgressPromotionOfProduct(product, now);

        return new OrderItem(product, quantity, promotion);
    }

    public String getProductName() {
        return this.product.getName();
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    public boolean isEligibleForPromotion() {
        if (promotion == null) {
            return false;
        }
        return promotion.isEligibleQuantity(quantity);
    }

    public void increaseQuantity() {
        this.quantity = quantity.increase(1);
    }

    public void decreaseQuantity(int amount) {
        this.quantity = quantity.decrease(amount);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public void setFreeQuantity(int promotionAppliedQuantity) {
        int freeQuantity = promotion.getFreeQuantity(promotionAppliedQuantity);
        this.freeQuantity = quantity.increase(freeQuantity);
    }
}
