package store.dto;

import store.model.Product;
import store.model.PromotionProduct;

public record ProductInformation(String name, int price, int stockCount, String promotionName) {
    public static ProductInformation from(Product product) {
        String promotionName = null;
        if (product instanceof PromotionProduct promotionProduct) {
            promotionName = promotionProduct.getPromotion().name();
        }
        return new ProductInformation(
                product.getName(),
                product.getPrice(),
                product.getStockCount(),
                promotionName
        );
    }
}
