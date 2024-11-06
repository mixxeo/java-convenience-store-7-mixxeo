package store.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PromotionBuilder(String name, int buyQuantity, LocalDateTime startDate, LocalDateTime endDate) {
    private static final int NAME_COLUMN = 0;
    private static final int BUY_QUANTITY_COLUMN = 1;
    private static final int START_DATE_COLUMN = 3;
    private static final int END_DATE_COLUMN = 4;

    public static PromotionBuilder from(String rawData) {
        List<String> fields = List.of(rawData.split(","));

        String name = fields.get(NAME_COLUMN);
        int buyQuantity = Integer.parseInt(fields.get(BUY_QUANTITY_COLUMN));
        LocalDateTime startDate = LocalDateTime.parse(fields.get(START_DATE_COLUMN));
        LocalDateTime endDate = LocalDateTime.parse(fields.get(END_DATE_COLUMN));

        return new PromotionBuilder(name, buyQuantity, startDate, endDate);
    }
}
