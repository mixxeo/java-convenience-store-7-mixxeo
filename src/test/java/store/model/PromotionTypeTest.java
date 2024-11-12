package store.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PromotionTypeTest {
    @ParameterizedTest
    @CsvSource({
            "1, true", "3, true", "5, true",
            "2, false", "4, false", "6, false"
    })
    @DisplayName("1+1 프로모션 추가 증정 여부를 수량에 따라 올바르게 반환한다.")
    void testIsEligibleQuantityForBuyOneGetOneFree(int quantityValue, boolean expectedResult) {
        Quantity quantity = Quantity.from(String.valueOf(quantityValue));
        boolean result = PromotionType.BUY_ONE_GET_ONE_FREE.isEligibleQuantity(quantity);
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
            "1, false", "4, false", "7, false",
            "2, true", "5, true", "8, true",
            "3, false", "6, false", "9, false"
    })
    @DisplayName("2+1 프로모션 추가 증정 여부를 수량에 따라 올바르게 반환한다.")
    void testIsEligibleQuantityForBuyTwoGetOneFree(int quantityValue, boolean expectedResult) {
        Quantity quantity = Quantity.from(String.valueOf(quantityValue));
        boolean result = PromotionType.BUY_TWO_GET_ONE_FREE.isEligibleQuantity(quantity);
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({ "5,4,3", "10,10,9" })
    @DisplayName("주어진 수량에서 최대로 프로모션을 적용할 수 있는 수량을 반환한다.")
    void testCalculateMaxAppliedCount(int count, int buyOneGetOneExpected, int buyTwoGetOneExpected) {
        int buyOneGetOneResult = PromotionType.BUY_ONE_GET_ONE_FREE.calculateMaxAppliedCount(count);
        int buyTwoGetOneResult = PromotionType.BUY_TWO_GET_ONE_FREE.calculateMaxAppliedCount(count);
        assertAll(
                () -> assertThat(buyOneGetOneResult).isEqualTo(buyOneGetOneExpected),
                () -> assertThat(buyTwoGetOneResult).isEqualTo(buyTwoGetOneExpected)
        );
    }

    @ParameterizedTest
    @CsvSource({ "5,2,1", "10,5,3" })
    @DisplayName("주어진 수량에서 무료로 증정되는 상품의 개수를 반환한다.")
    void testCalculateFreeQuantity(int count, int buyOneGetOneExpected, int buyTwoGetOneExpected) {
        int buyOneGetOneResult = PromotionType.BUY_ONE_GET_ONE_FREE.calculateFreeQuantity(count);
        int buyTwoGetOneResult = PromotionType.BUY_TWO_GET_ONE_FREE.calculateFreeQuantity(count);
        assertAll(
                () -> assertThat(buyOneGetOneResult).isEqualTo(buyOneGetOneExpected),
                () -> assertThat(buyTwoGetOneResult).isEqualTo(buyTwoGetOneExpected)
        );
    }
}
