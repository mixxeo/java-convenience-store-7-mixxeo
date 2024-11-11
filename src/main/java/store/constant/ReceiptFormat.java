package store.constant;

public enum ReceiptFormat {
    PRODUCT_ITEM_HEADER(String.format("%-10s\t%10s\t%8s%n", "상품명", "수량", "금액")),
    PRODUCT_ITEM("\t\t\t%,-10d%,d%n"),
    PROMOTION_ITEM("\t\t\t%,-10d%n"),
    TOTAL_PRICE("%-6s\t\t\t%,-10d%,d%n"),
    PROMOTION_DISCOUNT("%-5s\t\t\t\t\t\t  -%,-12d%n"),
    MEMBERSHIP_DISCOUNT("%-5s\t\t\t\t\t\t  -%,-12d%n"),
    PAID_AMOUNT("%-6s\t\t\t\t\t\t  %,6d%n")
    ;
    private final String format;

    ReceiptFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
