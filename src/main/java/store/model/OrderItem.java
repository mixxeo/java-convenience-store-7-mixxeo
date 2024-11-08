package store.model;

import camp.nextstep.edu.missionutils.DateTimes;

public class OrderItem {
    private final Product product;
    private final Quantity quantity;
    private final Quantity freeQuantity;

    private OrderItem(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
        this.freeQuantity = Quantity.from("0");
    }

    public static OrderItem of(String productName, Quantity quantity, ProductManager productManager) {
        productManager.validateHasProduct(productName);
        Product product = productManager.findByName(productName);
        productManager.validateStockAvailability(productManager.findByName(productName), quantity, DateTimes.now());

        return new OrderItem(product, quantity);
    }

    public String getProductName() {
        return this.product.getName();
    }
}
