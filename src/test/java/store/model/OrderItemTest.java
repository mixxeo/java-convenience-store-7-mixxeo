package store.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;

public class OrderItemTest {
    private ProductManager productManager;

    @BeforeEach
    void setUp() {
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
        productManager = new ProductManager(List.of(product, promotionProduct), stockManager);
    }

    @Test
    @DisplayName("입력값을 가진 객체가 생성된다.")
    void testCreateOrderItemIfInputIsValid() {
        Quantity quantity = Quantity.from("1");
        OrderItem orderItem = OrderItem.of("콜라", quantity, productManager);
        assertAll(
                () -> assertThat(orderItem.getProductName()).isEqualTo("콜라"),
                () -> assertThat(orderItem.getQuantity().value()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("존재하지 않는 상품명을 입력하면 예외가 발생한다.")
    void testThrowExceptionIfProductIsNotExisting() {
        Quantity quantity = Quantity.from("10");
        assertThatThrownBy(() -> OrderItem.of("환타", quantity, productManager))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ORDER_NOT_EXISTING_PRODUCT.getMessage());
    }

    @Test
    @DisplayName("재고 수량보다 구매 수량이 많으면 예외가 발생한다.")
    void testThrowExceptionIfQuantityIsMoreThanStock() {
        Quantity quantity = Quantity.from("20");
        assertThatThrownBy(() -> OrderItem.of("콜라", quantity, productManager))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ORDER_MORE_THAN_STOCK_COUNT.getMessage());
    }

    @Test
    @DisplayName("구매 수량과 상품 가격을 곱한 구매 금액을 반환한다.")
    void testCalculatePrice() {
        Quantity quantity = Quantity.from("10");
        OrderItem orderItem = OrderItem.of("콜라", quantity, productManager);
        int expected = quantity.value() * productManager.findByName("콜라").price();
        assertThat(orderItem.calculatePrice()).isEqualTo(expected);
    }
}
