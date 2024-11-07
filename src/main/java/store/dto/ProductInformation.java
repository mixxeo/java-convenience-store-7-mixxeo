package store.dto;

import store.model.Product;

public record ProductInformation(String name, int price, int stockCount, String promotionName) {
    public static ProductInformation of(Product product) {
        return new ProductInformation(
                product.getName(),
                product.getPrice(),
                product.getStockCount(),
                null
        );
    }

    public static ProductInformation ofPromotion(Product product) {
        return new ProductInformation(
                product.getName(),
                product.getPrice(),
                product.getPromotionStockCount(),
                product.getPromotion().name()
        );
    }
}
