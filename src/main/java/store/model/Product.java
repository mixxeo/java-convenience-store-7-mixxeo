package store.model;

import store.dto.ProductBuilder;

public class Product {
    private final String name;
    private final int price;
    private final int stockCount;
    private final Promotion promotion;
    private final int promotionStockCount;

    public Product(ProductBuilder builder) {
        this.name = builder.name();
        this.price = builder.price();
        this.stockCount = builder.stockCount();
        this.promotion = builder.promotion();
        this.promotionStockCount = builder.promotionStockCount();
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

    public int getStockCount() {
        return stockCount;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getPromotionStockCount() {
        return promotionStockCount;
    }
}
