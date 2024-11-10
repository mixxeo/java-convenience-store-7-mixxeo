package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class OrderItem {
    private final Product product;
    private Quantity quantity;
    private final Promotion promotion;
    private Quantity freeQuantity;

    private OrderItem(final Product product, final Quantity quantity, final Promotion promotion) {
        this.product = product;
        this.quantity = quantity;
        this.promotion = promotion;
        this.freeQuantity = Quantity.createInitialQuantity();
    }

    public static OrderItem of(
            final String productName,
            final Quantity quantity,
            final ProductManager productManager
    ) {
        LocalDate now = DateTimes.now().toLocalDate();

        productManager.validateHasProduct(productName);
        Product product = productManager.findByName(productName);
        productManager.validateStockAvailability(product, quantity, now);
        Promotion promotion = productManager.getInProgressPromotionOfProduct(product, now);

        return new OrderItem(product, quantity, promotion);
    }

    public int calculatePrice() {
        return this.quantity.value() * this.product.getPrice();
    }

    public int calculateFreePrice() {
        return this.freeQuantity.value() * this.product.getPrice();
    }

    public boolean isEligibleForPromotion() {
        if (promotion == null) {
            return false;
        }
        return promotion.isEligibleQuantity(quantity);
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public String getProductName() {
        return this.product.getName();
    }

    public Product getProduct() {
        return this.product;
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    public void increaseQuantity() {
        this.quantity = quantity.increase(1);
    }

    public void decreaseQuantity(final int amount) {
        this.quantity = quantity.decrease(amount);
    }

    public Quantity getFreeQuantity() {
        return this.freeQuantity;
    }

    public void setFreeQuantity(final int promotionAppliedQuantity) {
        int freeQuantityValue = promotion.getFreeQuantity(promotionAppliedQuantity);
        this.freeQuantity = freeQuantity.increase(freeQuantityValue);
    }
}
