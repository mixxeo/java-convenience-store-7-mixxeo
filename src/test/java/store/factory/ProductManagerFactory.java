package store.factory;

import java.time.LocalDate;
import java.util.List;
import store.model.Product;
import store.model.ProductManager;
import store.model.Promotion;
import store.model.PromotionType;
import store.model.StockManager;

public class ProductManagerFactory {
    public static ProductManager create() {
        Product product = new Product("콜라", 1000, null);
        Promotion promotion = new Promotion(
                "1+1",
                PromotionType.BUY_ONE_GET_ONE_FREE,
                LocalDate.of(1999, 1, 1),
                LocalDate.of(2099, 12, 31)
        );
        Product promotionProduct = new Product("사이다", 2000, promotion);
        StockManager stockManager = new StockManager();
        stockManager.addStock(product, 10, 0);
        stockManager.addStock(promotionProduct, 10, 20);
        return new ProductManager(List.of(product, promotionProduct), stockManager);
    }
}
