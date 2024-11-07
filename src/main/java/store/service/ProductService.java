package store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.ProductBuilder;
import store.dto.ProductFields;
import store.dto.PromotionBuilder;
import store.model.Product;
import store.model.ProductManager;
import store.model.Promotion;
import store.util.FileManager;

public class ProductService {
    private static final String PROMOTION_RESOURCE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_RESOURCE_PATH = "src/main/resources/products.md";
    private static final String COLUMN_SEPARATOR = ",";

    public ProductManager createProducts() {
        Map<String, Promotion> promotions = createPromotions();
        return createProducts(promotions);
    }

    private Map<String, Promotion>  createPromotions() {
        List<String> promotionsData = loadData(PROMOTION_RESOURCE_PATH);
        Map<String, Promotion> promotions = new HashMap<>();
        promotionsData.stream()
                .map(this::parseData)
                .map(PromotionBuilder::from)
                .map(Promotion::from)
                .forEach(promotion -> promotions.put(promotion.name(), promotion));
        return promotions;
    }

    private List<String> loadData(String filePath) {
        List<String> fileContent = FileManager.read(filePath);
        FileManager.removeHeader(fileContent);
        return fileContent;
    }

    private List<String> parseData(String rawData) {
        return List.of(rawData.split(COLUMN_SEPARATOR));
    }

    private ProductManager createProducts(Map<String, Promotion> promotions) {
        List<String> productsData = loadData(PRODUCT_RESOURCE_PATH);
        List<ProductBuilder> builders = generateProductBuilders(productsData, promotions);
        List<Product> products = builders.stream()
                .map(Product::new)
                .toList();
        return new ProductManager(products);
    }

    private List<ProductBuilder> generateProductBuilders(List<String> productsData, Map<String, Promotion> promotions) {
        Map<String, List<ProductFields>> groupByName = productsData.stream()
                .map(ProductFields::from)
                .collect(Collectors.groupingBy(ProductFields::name));

        return groupByName.values().stream()
                .map(fields -> ProductBuilder.of(fields, promotions))
                .toList();
    }
}
