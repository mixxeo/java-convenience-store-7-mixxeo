package store.model;

import store.dto.ProductBuilder;

public class Product {
    private final String name;
    private final int price;
    private final int stockCount;

    public Product(ProductBuilder builder) {
        this.name = builder.getName();
        this.price = builder.getPrice();
        this.stockCount = builder.getStockCount();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStockCount() {
        return stockCount;
    }
}
