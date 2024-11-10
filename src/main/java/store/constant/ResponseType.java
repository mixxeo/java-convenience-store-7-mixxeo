package store.constant;

public enum ResponseType {
    YES("Y"),
    NO("N");

    private final String value;

    ResponseType(String value) {
        this.value = value;
    }

    public static ResponseType fromString(String input) {
        validate(input);
        if (input.equals(YES.value)) {
            return YES;
        }
        return NO;
    }

    private static void validate(String input) {
        if (!isValid(input)) {
            throw new IllegalArgumentException(ExceptionMessage.INPUT_INVALID_VALUE.getMessage());
        }
    }

    private static boolean isValid(String input) {
        return input.equals(YES.value) || input.equals(NO.value);
    }
}
