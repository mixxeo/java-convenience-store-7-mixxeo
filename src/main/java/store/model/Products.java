package store.model;

import java.util.List;

public record Products(List<Product> products) {
    public Product findByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
