package store.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PromotionTest {
    @Test
    @DisplayName("시작전 프로모션에 대해 진행 여부는 false를 반환한다.")
    void testIsInProgressFalseIfPromotionIsNotStarted() {
        Promotion notStartedPromotion = new Promotion(
                "1+1",
                PromotionType.BUY_ONE_GET_ONE_FREE,
                LocalDate.of(2024,11,10),
                LocalDate.of(2024,11,20)
        );
        LocalDate testDate = LocalDate.of(2024,11,9);
        assertThat(notStartedPromotion.isInProgress(testDate)).isFalse();
    }

    @Test
    @DisplayName("진행중 프로모션에 대해 진행 여부는 true를 반환한다.")
    void testIsInProgressTrueIfPromotionIsInProgress() {
        Promotion inProgressPromotion = new Promotion(
                "1+1",
                PromotionType.BUY_ONE_GET_ONE_FREE,
                LocalDate.of(2024,11,10),
                LocalDate.of(2024,11,20)
        );
        LocalDate testDate = LocalDate.of(2024,11,15);
        assertThat(inProgressPromotion.isInProgress(testDate)).isTrue();
    }

    @Test
    @DisplayName("종료된 프로모션에 대해 진행 여부는 false를 반환한다.")
    void testIsInProgressFalseIfPromotionIsEnded() {
        Promotion endedPromotion = new Promotion(
                "1+1",
                PromotionType.BUY_ONE_GET_ONE_FREE,
                LocalDate.of(2024,11,10),
                LocalDate.of(2024,11,20)
        );
        LocalDate testDate = LocalDate.of(2024,11,25);
        assertThat(endedPromotion.isInProgress(testDate)).isFalse();
    }
}
