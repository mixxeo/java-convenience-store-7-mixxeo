package store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.dto.PromotionBuilder;
import store.model.Product;
import store.model.Promotion;
import store.util.FileManager;

public class StoreController {
    public void run() {
        List<Product> products = initialize();
    }

    private List<Product> initialize() {
        List<Promotion> promotions = createPromotions();
        return createProducts(promotions);
    }

    private List<Promotion> createPromotions() {
        List<String> promotionsData = loadData("src/main/resources/promotions.md");
        return promotionsData.stream()
                .map(PromotionBuilder::from)
                .map(Promotion::from)
                .toList();
    }

    private List<String> loadData(String filePath) {
        List<String> fileContent = FileManager.read(filePath);
        FileManager.removeHeader(fileContent);
        return fileContent;
    }

    private List<Product> createProducts(List<Promotion> promotions) {
        List<String> productsData = loadData("src/main/resources/products.md");
        Map<String, Product.Builder> productBuilderMap = generateProductBuilders(productsData, promotions);
        return productBuilderMap.values().stream()
                .map(Product.Builder::build)
                .toList();
    }

    private Map<String, Product.Builder> generateProductBuilders(
            List<String> productsData,
            List<Promotion> promotions
    ) {
        Map<String, Product.Builder> productBuilderMap = new HashMap<>();
        for (String data : productsData) {
            String[] fields = data.split(",");
            String name = fields[0];
            int price = Integer.parseInt(fields[1]);
            int quantity = Integer.parseInt(fields[2]);
            Promotion promotion = findPromotion(fields[3], promotions);

            Product.Builder builder = createOrGetProductBuilder(name, price, productBuilderMap);
            updateProductBuilder(builder, quantity, promotion);
        }
        return productBuilderMap;
    }

    private Promotion findPromotion(String name, List<Promotion> promotions) {
        return promotions.stream()
                .filter(promotion -> promotion.name().equals(name))
                .findFirst()
                .orElse(null);
    }

    private Product.Builder createOrGetProductBuilder(
            String name,
            int price,
            Map<String, Product.Builder> builderMap
    ) {
        return builderMap.computeIfAbsent(name, k -> new Product.Builder().name(name).price(price));
    }

    private void updateProductBuilder(Product.Builder builder, int quantity, Promotion promotion) {
        if (promotion == null) {
            builder.quantity(quantity);
            return;
        }
        builder.promotionQuantity(quantity).promotion(promotion);
    }
}
