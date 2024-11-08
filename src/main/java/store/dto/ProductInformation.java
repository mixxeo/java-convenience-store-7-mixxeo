package store.dto;

import store.model.Product;

public record ProductInformation(String name, int price, int stockCount, String promotionName) {
    public static ProductInformation of(Product product, int stockCount) {
        return new ProductInformation(
                product.getName(),
                product.getPrice(),
                stockCount,
                null
        );
    }

    public static ProductInformation ofPromotion(Product product, int stockCount) {
        return new ProductInformation(
                product.getName(),
                product.getPrice(),
                stockCount,
                product.getPromotion().name()
        );
    }
}
