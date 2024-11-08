package store.model;

import store.constant.ExceptionMessage;

public record Quantity(int value) {
    private static final String NUMBER_PATTERN = "\\d+";

    public static Quantity createInitialQuantity() {
        return new Quantity(0);
    }

    public static Quantity from(String input) {
        validateNumberFormat(input);
        validatePositiveIntegerRange(input);
        return new Quantity(Integer.parseInt(input));
    }

    private static void validateNumberFormat(final String input) {
        if (isNotNumber(input)) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
        }
    }

    private static boolean isNotNumber(final String input) {
        return !input.matches(NUMBER_PATTERN);
    }

    private static void validatePositiveIntegerRange(final String input) {
        try {
            int value = Integer.parseInt(input);
            validatePositive(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }

    private static void validatePositive(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }

    public Quantity increase(int amount) {
        return new Quantity(value + amount);
    }

    public Quantity decrease(int amount) {
        return new Quantity(value - amount);
    }
}
