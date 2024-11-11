package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;
import store.factory.ProductManagerFactory;

public class OrderTest {
    private ProductManager productManager;

    @BeforeEach
    void setUp() {
        productManager = ProductManagerFactory.create();
    }

    @Test
    @DisplayName("중복된 구매 상품을 입력하면 예외가 발생한다.")
    void testThrowExceptionIfItemsAreDuplicated() {
        Quantity quantity = Quantity.from("1");
        OrderItem orderItem1 = OrderItem.of("콜라", quantity, productManager);
        OrderItem orderItem2 = OrderItem.of("콜라", quantity, productManager);
        assertThatThrownBy(() -> new Order(List.of(orderItem1, orderItem2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
    }

    @Test
    @DisplayName("총 구매 수량을 계산한다.")
    void testCalculateTotalQuantity() {
        Quantity quantity = Quantity.from("5");
        OrderItem orderItem1 = OrderItem.of("콜라", quantity, productManager);
        OrderItem orderItem2 = OrderItem.of("사이다", quantity, productManager);
        Order order = new Order(List.of(orderItem1, orderItem2));
        assertThat(order.calculateTotalQuantity()).isEqualTo(quantity.value() * 2);
    }

    @Test
    @DisplayName("총 구매 가격을 계산한다.")
    void testCalculateTotalPrice() {
        Quantity quantity = Quantity.from("5");
        OrderItem orderItem1 = OrderItem.of("콜라", quantity, productManager);
        OrderItem orderItem2 = OrderItem.of("사이다", quantity, productManager);
        Order order = new Order(List.of(orderItem1, orderItem2));

        int expected = productManager.findByName("콜라").price() * quantity.value()
                + productManager.findByName("사이다").price() * quantity.value();
        assertThat(order.calculateTotalPrice()).isEqualTo(expected);
    }

    @Test
    @DisplayName("총 프로모션 할인 금액을 계산한다.")
    void testCalculatePromotionDiscount() {
        Quantity quantity = Quantity.from("5");
        OrderItem orderItem1 = OrderItem.of("콜라", quantity, productManager);
        OrderItem orderItem2 = OrderItem.of("사이다", quantity, productManager);
        orderItem2.setFreeQuantity(2);
        Order order = new Order(List.of(orderItem1, orderItem2));

        int expected = productManager.findByName("사이다").price() * 2;
        assertThat(order.calculatePromotionDiscount()).isEqualTo(expected);
    }

    @Test
    @DisplayName("멤버십 할인 금액을 계산한다.")
    void testCalculateMembershipDiscount() {
        Quantity quantity = Quantity.from("5");
        OrderItem orderItem1 = OrderItem.of("콜라", quantity, productManager);
        OrderItem orderItem2 = OrderItem.of("사이다", quantity, productManager);
        orderItem2.setFreeQuantity(2);
        Order order = new Order(List.of(orderItem1, orderItem2));

        int expected = (int) Math.floor(orderItem1.calculatePrice() * 0.3);
        assertThat(order.calculateMembershipDiscount(true)).isEqualTo(expected);
    }
}
