package store.controller;

import java.util.List;
import store.dto.ProductBuilder;
import store.dto.PromotionBuilder;
import store.dto.PromotionProductBuilder;
import store.model.Product;
import store.model.Products;
import store.model.Promotion;
import store.model.PromotionProduct;
import store.model.Promotions;
import store.util.FileManager;

public class StoreController {
    public void run() {
        Products products = initialize();
    }

    private Products initialize() {
        Promotions promotions = createPromotions();
        return createProducts(promotions);
    }

    private Promotions createPromotions() {
        List<String> promotionsData = loadData("src/main/resources/promotions.md");
        List<Promotion> promotions = promotionsData.stream()
                .map(this::parseData)
                .map(PromotionBuilder::from)
                .map(Promotion::from)
                .toList();
        return new Promotions(promotions);
    }

    private List<String> loadData(String filePath) {
        List<String> fileContent = FileManager.read(filePath);
        FileManager.removeHeader(fileContent);
        return fileContent;
    }

    private List<String> parseData(String rawData) {
        return List.of(rawData.split(","));
    }

    private Products createProducts(Promotions promotions) {
        List<String> productsData = loadData("src/main/resources/products.md");
        List<ProductBuilder> builders = generateProductBuilders(productsData, promotions);
        List<Product> products = builders.stream()
                .map(this::createProduct)
                .toList();
        return new Products(products);
    }

    private List<ProductBuilder> generateProductBuilders(List<String> productsData, Promotions promotions) {
        return productsData.stream()
                .map(this::parseData)
                .map(fields -> ProductBuilder.of(fields, promotions))
                .toList();
    }

    private Product createProduct(ProductBuilder builder) {
        if (builder instanceof PromotionProductBuilder) {
            return new PromotionProduct((PromotionProductBuilder) builder);
        }
        return new Product(builder);
    }
}
