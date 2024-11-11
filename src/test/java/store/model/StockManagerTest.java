package store.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockManagerTest {
    private Product product1;
    private Product product2;
    private StockManager stockManager;

    @BeforeEach
    void setUp() {
        product1 = new Product("콜라", 1000, null);
        Promotion promotion = new Promotion(
                "1+1",
                PromotionType.BUY_ONE_GET_ONE_FREE,
                LocalDate.of(1999, 1, 1),
                LocalDate.of(2099, 12, 31)
        );
        product2 = new Product("사이다", 2000, promotion);
        stockManager = new StockManager();
        stockManager.addStock(product1, 10, 0);
        stockManager.addStock(product2, 10, 10);
    }

    @Test
    @DisplayName("구매 수량이 재고보다 많으면 재고 부족 여부 true를 반환한다.")
    void testReturnTrueIfQuantityIsMoreThanStock() {
        Quantity quantity = Quantity.from("20");
        assertThat(stockManager.isInSufficientStock(product1, quantity, DateTimes.now().toLocalDate())).isTrue();
    }

    @Test
    @DisplayName("구매 수량이 재고보다 적으면 재고 부족 여부 false를 반환한다.")
    void testReturnFalseIfQuantityIsLessThanStock() {
        Quantity quantity = Quantity.from("1");
        assertThat(stockManager.isInSufficientStock(product1, quantity, DateTimes.now().toLocalDate())).isFalse();
    }


    @Test
    @DisplayName("부족한 프로모션 재고 수량을 계산한다.")
    void testCalculateInSufficientPromotionStock() {
        Quantity quantity = Quantity.from("15");
        assertThat(stockManager.calculateInSufficientPromotionStock(product2, quantity)).isEqualTo(5);
    }

    @Test
    @DisplayName("프로모션 재고 내에서, 구매 수량 중 프로모션 적용 수량을 계산한다.")
    void testCalculatePromotionAppliedQuantity() {
        Quantity quantity = Quantity.from("15");
        assertThat(stockManager.calculatePromotionAppliedQuantity(product2, quantity)).isEqualTo(10);
    }

    @Test
    @DisplayName("재고에서 주문 수량을 차감시킨다.")
    void testDeductStock() {
        ProductManager productManager = new ProductManager(List.of(product1, product2), stockManager);
        Quantity quantity = Quantity.from("5");
        OrderItem orderItem = OrderItem.of(product1.name(), quantity, productManager);
        Order order = new Order(List.of(orderItem));
        stockManager.deductStock(order);
        assertThat(stockManager.getNormalStock(product1)).isEqualTo(5);
    }

    @Test
    @DisplayName("프로모션 적용 상품의 재고는 프로모션 재고를 우선으로 차감한다.")
    void testDeductPromotionStockFirstIfProductHasPromotion() {
        ProductManager productManager = new ProductManager(List.of(product1, product2), stockManager);
        Quantity quantity = Quantity.from("5");
        OrderItem orderItem = OrderItem.of(product2.name(), quantity, productManager);
        Order order = new Order(List.of(orderItem));
        stockManager.deductStock(order);
        assertAll(
                () -> assertThat(stockManager.getNormalStock(product2)).isEqualTo(10),
                () -> assertThat(stockManager.getPromotionStock(product2)).isEqualTo(5)
        );
    }
}
