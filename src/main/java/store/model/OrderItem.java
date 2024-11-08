package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class OrderItem {
    private final Product product;
    private Quantity quantity;
    private final Promotion promotion;
    private final Quantity freeQuantity;

    private OrderItem(Product product, Quantity quantity, Promotion promotion) {
        this.product = product;
        this.quantity = quantity;
        this.promotion = promotion;
        this.freeQuantity = Quantity.from("0");
    }

    public static OrderItem of(String productName, Quantity quantity, ProductManager productManager) {
        LocalDate now = DateTimes.now().toLocalDate();

        productManager.validateHasProduct(productName);
        Product product = productManager.findByName(productName);
        productManager.validateStockAvailability(productManager.findByName(productName), quantity, now);
        Promotion promotion = productManager.getInProgressPromotionOfProduct(product, now);

        return new OrderItem(product, quantity, promotion);
    }

    public String getProductName() {
        return this.product.getName();
    }

    public boolean isEligibleForPromotion() {
        if (promotion == null) {
            return false;
        }
        return promotion.isEligibleQuantity(quantity);
    }

    public void increaseQuantity() {
        this.quantity = quantity.increase();
    }
}
