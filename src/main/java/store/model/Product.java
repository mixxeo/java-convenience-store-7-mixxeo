package store.model;

import store.dto.ProductBuilder;

public class Product {
    private final String name;
    private final int price;
    private final Promotion promotion;

    public Product(ProductBuilder builder) {
        this.name = builder.name();
        this.price = builder.price();
        this.promotion = builder.promotion();
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        return promotion;
    }
}
