package store.dto;

import store.model.Product;

public record CatalogEntry(String name, int price, int stockCount, String promotionName) {
    public static CatalogEntry of(final Product product, final int stockCount) {
        return new CatalogEntry(
                product.getName(),
                product.getPrice(),
                stockCount,
                null
        );
    }

    public static CatalogEntry ofPromotion(final Product product, final int stockCount) {
        return new CatalogEntry(
                product.getName(),
                product.getPrice(),
                stockCount,
                product.getPromotion().name()
        );
    }
}
