package store.model;

import java.util.LinkedHashMap;
import store.constant.ExceptionMessage;

public class Order {
    private final LinkedHashMap<Product, Quantity> items;
    public Order() {
        this.items = new LinkedHashMap<>();
    }

    public void addItem(Product product, Quantity quantity) {
        validateIsDuplicatedItem(product);
        items.put(product, quantity);
    }

    private void validateIsDuplicatedItem(Product product) {
        if (items.containsKey(product)) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }
}
