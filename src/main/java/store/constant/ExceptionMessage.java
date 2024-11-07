package store.constant;

public enum ExceptionMessage {
    INPUT_INVALID_VALUE("잘못된 입력입니다. 다시 입력해 주세요."),
    ORDER_INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
