package store.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;
import store.factory.ProductManagerFactory;

public class OrderItemTest {
    private ProductManager productManager;

    @BeforeEach
    void setUp() {
        productManager = ProductManagerFactory.create();
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

    @Test
    @DisplayName("증정 수량으로 할인 받은 금액을 계산한다.")
    void testCalculateFreePrice() {
        Quantity quantity = Quantity.from("10");
        OrderItem orderItem = OrderItem.of("사이다", quantity, productManager);
        orderItem.setFreeQuantity(5);
        int expected = 5 * productManager.findByName("사이다").price();
        assertThat(orderItem.calculateFreePrice()).isEqualTo(expected);
    }
}
