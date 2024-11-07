package store.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record PromotionBuilder(String name, int buyQuantity, LocalDate startDate, LocalDate endDate) {
    private static final int NAME_COLUMN = 0;
    private static final int BUY_QUANTITY_COLUMN = 1;
    private static final int START_DATE_COLUMN = 3;
    private static final int END_DATE_COLUMN = 4;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static PromotionBuilder from(List<String> fields) {
        String name = fields.get(NAME_COLUMN);
        int buyQuantity = Integer.parseInt(fields.get(BUY_QUANTITY_COLUMN));
        LocalDate startDate = convertToLocalDate(fields.get(START_DATE_COLUMN));
        LocalDate endDate = convertToLocalDate(fields.get(END_DATE_COLUMN));

        return new PromotionBuilder(name, buyQuantity, startDate, endDate);
    }

    public static LocalDate convertToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateString, formatter);
    }
}
