package store.model;

import java.util.LinkedHashMap;
import store.constant.ExceptionMessage;

public class Order {
    private final LinkedHashMap<String, Quantity> items;
    public Order() {
        this.items = new LinkedHashMap<>();
    }

    public void addItem(String productName, Quantity quantity) {
        validateIsDuplicatedItem(productName);
        items.put(productName, quantity);
    }

    private void validateIsDuplicatedItem(String productName) {
        if (items.containsKey(productName)) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }
}
