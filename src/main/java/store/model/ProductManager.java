package store.model;

import java.util.List;
import store.constant.ExceptionMessage;

public record ProductManager(List<Product> products) {
    public void validateHasProduct(String name) {
        if (findByName(name) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
        }
    }

    private Product findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
