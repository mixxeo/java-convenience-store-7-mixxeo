package store.controller;

import java.util.List;
import store.util.FileManager;

public class StoreController {
    public void run() {
        initialize();
    }

    private void initialize() {
        createPromotions();
        createProducts();
    }

    private void createPromotions() {
        List<String> promotionsContent = loadData("src/main/resources/promotions.md");
    }

    private void createProducts() {
        List<String> promotionsContent = loadData("src/main/resources/products.md");
    }

    private List<String> loadData(String filePath) {
        List<String> fileContent = FileManager.read(filePath);
        FileManager.removeHeader(fileContent);
        return fileContent;
    }
}
