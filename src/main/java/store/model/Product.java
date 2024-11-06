package store.model;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final int promotionQuantity;

    private final Promotion promotion;

    public Product(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.quantity = builder.quantity;
        this.promotionQuantity = builder.promotionQuantity;
        this.promotion = builder.promotion;
    }

    public static class Builder {
        private String name;
        private int price;
        private int quantity;
        private int promotionQuantity;
        private Promotion promotion;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(int price) {
            this.price = price;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder promotionQuantity(int promotionQuantity) {
            this.promotionQuantity = promotionQuantity;
            return this;
        }

        public Builder promotion(Promotion promotion) {
            this.promotion = promotion;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
