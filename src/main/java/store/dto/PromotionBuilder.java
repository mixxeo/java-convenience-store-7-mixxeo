package store.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import store.model.PromotionType;

public record PromotionBuilder(String name, PromotionType type, LocalDate startDate, LocalDate endDate) {
    private static final int NAME_COLUMN = 0;
    private static final int BUY_QUANTITY_COLUMN = 1;
    private static final int START_DATE_COLUMN = 3;
    private static final int END_DATE_COLUMN = 4;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static PromotionBuilder from(final List<String> fields) {
        String name = fields.get(NAME_COLUMN);
        int buyQuantity = Integer.parseInt(fields.get(BUY_QUANTITY_COLUMN));
        PromotionType type = PromotionType.getType(buyQuantity);
        LocalDate startDate = convertToLocalDate(fields.get(START_DATE_COLUMN));
        LocalDate endDate = convertToLocalDate(fields.get(END_DATE_COLUMN));

        return new PromotionBuilder(name, type, startDate, endDate);
    }

    public static LocalDate convertToLocalDate(final String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateString, formatter);
    }
}
