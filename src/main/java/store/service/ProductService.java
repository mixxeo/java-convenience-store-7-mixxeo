package store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.CatalogEntry;
import store.dto.ProductBuilder;
import store.dto.ProductFields;
import store.dto.PromotionBuilder;
import store.model.Product;
import store.model.ProductManager;
import store.model.Promotion;
import store.model.StockManager;
import store.util.FileManager;

public class ProductService {
    private static final String PROMOTION_RESOURCE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCT_RESOURCE_PATH = "src/main/resources/products.md";
    private static final String COLUMN_SEPARATOR = ",";

    public ProductManager createProductManager() {
        Map<String, Promotion> promotions = loadPromotions();
        LinkedHashMap<String, List<ProductFields>> productFieldsByName = loadProductData();
        List<Product> products = createProducts(productFieldsByName, promotions);
        StockManager stockManager = initializeStockManager(productFieldsByName, products);

        return new ProductManager(products, stockManager);
    }

    public List<CatalogEntry> convertToCatalogEntries(Product product, ProductManager productManager) {
        List<CatalogEntry> catalogEntries = new ArrayList<>();
        if (product.hasPromotion()) {
            catalogEntries.add(CatalogEntry.ofPromotion(product, productManager.getPromotionStock(product)));
        }
        catalogEntries.add(CatalogEntry.of(product, productManager.getStock(product)));
        return catalogEntries;
    }

    private Map<String, Promotion> loadPromotions() {
        List<String> promotionsData = loadData(PROMOTION_RESOURCE_PATH);
        Map<String, Promotion> promotions = new HashMap<>();
        promotionsData.stream()
                .map(this::parseData)
                .map(PromotionBuilder::from)
                .map(Promotion::new)
                .forEach(promotion -> promotions.put(promotion.name(), promotion));
        return promotions;
    }

    private LinkedHashMap<String, List<ProductFields>> loadProductData() {
        List<String> productsData = loadData(PRODUCT_RESOURCE_PATH);
        return productsData.stream()
                .map(this::parseData)
                .map(ProductFields::from)
                .collect(Collectors.groupingBy(ProductFields::name, LinkedHashMap::new, Collectors.toList()));
    }

    private List<String> loadData(String filePath) {
        List<String> fileContent = FileManager.read(filePath);
        FileManager.removeHeader(fileContent);
        return fileContent;
    }

    private List<String> parseData(String rawData) {
        return List.of(rawData.split(COLUMN_SEPARATOR));
    }

    private List<Product> createProducts(
            LinkedHashMap<String, List<ProductFields>> productFieldsByName,
            Map<String, Promotion> promotions
    ) {
        return productFieldsByName.values().stream()
                .map(fields -> generateProductBuilder(fields, promotions))
                .map(Product::new)
                .toList();
    }

    private ProductBuilder generateProductBuilder(List<ProductFields> fields, Map<String, Promotion> promotions) {
        ProductFields normalFields = findProductFieldsByPromotionStatus(fields, false);
        ProductFields promotionFields = findProductFieldsByPromotionStatus(fields, true);

        if (promotionFields == null) {
            return ProductBuilder.createWithoutPromotion(normalFields);
        }
        return ProductBuilder.createWithPromotion(promotionFields, promotions);
    }

    private StockManager initializeStockManager(
            LinkedHashMap<String, List<ProductFields>> productFieldsByName,
            List<Product> products
    ) {
        StockManager stockManager = new StockManager();
        for (Product product : products) {
            List<ProductFields> fields = productFieldsByName.get(product.getName());
            int stockCount = getStockCount(fields, false);
            int promotionStockCount = getStockCount(fields, true);
            stockManager.addStock(product, stockCount, promotionStockCount);
        }
        return stockManager;
    }

    private int getStockCount(List<ProductFields> fields, boolean isPromotion) {
        ProductFields field = findProductFieldsByPromotionStatus(fields, isPromotion);
        if (field == null) {
            return 0;
        }
        return field.stockCount();
    }

    private static ProductFields findProductFieldsByPromotionStatus(
            List<ProductFields> productFields,
            boolean hasPromotion
    ) {
        return productFields.stream()
                .filter(fields -> fields.hasPromotion() == hasPromotion)
                .findFirst()
                .orElse(null);
    }
}
