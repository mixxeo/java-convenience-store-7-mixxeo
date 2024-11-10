package store.constant;

public enum ResponseType {
    YES("Y"),
    NO("N");

    private final String value;

    ResponseType(String value) {
        this.value = value;
    }

    public static boolean isValid(String input) {
        return input.equals(YES.value) || input.equals(NO.value);
    }

    public static ResponseType fromString(String input) {
        if (input.equals(YES.value)) {
            return YES;
        }
        return NO;
    }
}
