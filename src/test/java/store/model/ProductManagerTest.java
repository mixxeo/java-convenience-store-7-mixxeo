package store.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;

public class ProductManagerTest {
    @Test
    @DisplayName("모든 상품의 수량이 0이면 예외가 발생한다.")
    void testThrowExceptionIfAllProductsAreOutOfStock() {
        Product product1 = new Product("콜라", 1000, null);
        Product product2 = new Product("사이다", 1000, null);
        StockManager stockManager = new StockManager();
        stockManager.addStock(product1, 0, 0);
        stockManager.addStock(product2, 0, 0);
        ProductManager productManager = new ProductManager(List.of(product1, product2), stockManager);

        assertThatThrownBy(productManager::validateProductsInStock)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(ExceptionMessage.ALL_PRODUCTS_OUT_OF_STOCK.getMessage());
    }
}
