package store.view;

import java.util.List;
import java.util.stream.Collectors;
import store.constant.OutputMessage;
import store.dto.CatalogEntry;
import store.dto.Receipt;
import store.dto.ReceiptEntry;

public class OutputView {
    public void printProductCatalog(List<CatalogEntry> productInformation) {
        printWelcomeMessage();
        String catalog = productInformation.stream()
                .map(this::buildProductMessage)
                .collect(Collectors.joining("\n"));
        System.out.println(catalog);
    }

    private void printWelcomeMessage() {
        System.out.println();
        System.out.println(OutputMessage.WELCOME_MESSAGE.getMessage());
    }

    private String buildProductMessage(CatalogEntry catalogEntry) {
        String name = catalogEntry.name();
        int price = catalogEntry.price();
        String stockCount = formatProductStockCount(catalogEntry.stockCount());
        String promotionName = catalogEntry.promotionName();

        if (promotionName == null) {
            return String.format(OutputMessage.PRODUCT_FORMAT.getMessage(), name, price, stockCount);
        }
        return String.format(OutputMessage.PROMOTION_PRODUCT_FORMAT.getMessage(), name, price, stockCount,
                promotionName);
    }

    private String formatProductStockCount(int stockCount) {
        if (stockCount == 0) {
            return OutputMessage.OUT_OF_STOCK.getMessage();
        }
        return String.format(OutputMessage.PRODUCT_STOCK_COUNT_FORMAT.getMessage(), stockCount);
    }

    public void printRequestOrder() {
        System.out.println();
        System.out.println(OutputMessage.ORDER_REQUEST_MESSAGE.getMessage());
    }

    public void printOfferFreeProduct(String productName) {
        System.out.println();
        System.out.printf(OutputMessage.FREE_PRODUCT_OFFER_FORMAT.getMessage(), productName);
    }

    public void printFullPriceQuantityNotification(String productName, int quantity) {
        System.out.println();
        System.out.printf(OutputMessage.FULL_PRICE_QUANTITY_NOTIFICATION_FORMAT.getMessage(), productName, quantity);
    }

    public void printSuggestMembershipSale() {
        System.out.println();
        System.out.println(OutputMessage.MEMBERSHIP_SALE_SUGGESTION.getMessage());
    }

    public void printReceipt(Receipt receipt) {
        StringBuilder receiptContents = new StringBuilder("\n")
                .append("==============W 편의점================\n")
                .append(buildProductItemHeader())
                .append(buildProductItems(receipt.entries()))
                .append("=============증\t\t정===============\n")
                .append(buildFreePromotionItems(receipt.entries()))
                .append("====================================\n")
                .append(buildPriceInformation(receipt));
        System.out.println(receiptContents);
    }

    private String buildProductItemHeader() {
        return String.format("%-10s\t%10s\t%8s%n", "상품명", "수량", "금액");
    }

    private String buildProductItems(List<ReceiptEntry> receiptEntries) {
        StringBuilder productItems = new StringBuilder();
        receiptEntries.forEach(entry -> {
            String format = "%-" + getPrintKoreanLength(entry.productName()) + "s\t\t\t%,-10d%,d%n";
            String itemContents = String.format(format, entry.productName(), entry.quantity(), entry.price());
            productItems.append(itemContents);
        });
        return productItems.toString();
    }

    private String buildFreePromotionItems(List<ReceiptEntry> receiptEntries) {
        StringBuilder freePromotionItems = new StringBuilder();
        receiptEntries.stream()
                .filter(entry -> entry.freeQuantity() > 0)
                .forEach(entry -> {
                    String format = "%-" + getPrintKoreanLength(entry.productName()) + "s\t\t\t%,d%n";
                    String itemContents = String.format(format, entry.productName(), entry.freeQuantity());
                    freePromotionItems.append(itemContents);
                });
        return freePromotionItems.toString();
    }

    private int getPrintKoreanLength(String string) {
        return 10 - string.length();
    }

    private String buildPriceInformation(Receipt receipt) {
        StringBuilder priceInformation = new StringBuilder()
                .append(String.format("%-6s\t\t\t%,-10d%s%n", "총구매액", receipt.totalQuantity(), receipt.totalPrice()))
                .append(String.format("%-5s\t\t\t\t\t\t  %-12s%n", "행사할인", receipt.promotionDiscount()))
                .append(String.format("%-5s\t\t\t\t\t\t  %-12s%n", "멤버십할인", receipt.memberShipDiscount()))
                .append(String.format("%-6s\t\t\t\t\t\t  %6s%n", "내실돈", receipt.paidAmount()));
        return priceInformation.toString();
    }

    public void printSuggestReorder() {
        System.out.println();
        System.out.println(OutputMessage.REORDER_SUGGESTION.getMessage());
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
