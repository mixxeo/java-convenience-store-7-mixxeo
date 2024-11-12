package store.constant;

public enum ExceptionMessage {
    INVALID_DATA_FILE("데이터 파일을 읽을 수 없습니다."),
    ALL_PRODUCTS_OUT_OF_STOCK("모든 상품이 품절되었습니다."),
    INPUT_INVALID_VALUE("잘못된 입력입니다. 다시 입력해 주세요."),
    ORDER_INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    ORDER_NOT_EXISTING_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    ORDER_MORE_THAN_STOCK_COUNT("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    private static final String PREFIX = "[ERROR] ";

    private final String message;

    ExceptionMessage(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return PREFIX + message;
    }
}
