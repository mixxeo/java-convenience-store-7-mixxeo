package store.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.constant.ExceptionMessage;

public class QuantityTest {
    @Test
    @DisplayName("입력값을 가진 객체가 생성된다.")
    void testCreateObjectIfValueIsValid() {
        int value = 10;
        Quantity quantity = Quantity.from(String.valueOf(value));
        assertThat(quantity.value()).isEqualTo(value);
    }

    @Test
    @DisplayName("숫자가 아닌 값을 입력하면 예외가 발생한다.")
    void testThrowExceptionIfValueIsNotNumber() {
        assertThatThrownBy(() -> Quantity.from("abc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
    }

    @Test
    @DisplayName("정수 범위가 아닌 값을 입력하면 예외가 발생한다.")
    void testThrowExceptionIfValueIsOverIntegerRange() {
        assertThatThrownBy(() -> Quantity.from("999999999999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
    }
}
