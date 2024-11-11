package store.view;

import java.util.List;
import java.util.stream.Collectors;
import store.constant.OutputMessage;
import store.constant.ReceiptFormat;
import store.dto.CatalogEntry;
import store.dto.Receipt;
import store.dto.ReceiptEntry;

public class OutputView {
    public void printProductCatalog(final List<CatalogEntry> productInformation) {
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

    private String buildProductMessage(final CatalogEntry catalogEntry) {
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

    private String formatProductStockCount(final int stockCount) {
        if (stockCount == 0) {
            return OutputMessage.OUT_OF_STOCK.getMessage();
        }
        return String.format(OutputMessage.PRODUCT_STOCK_COUNT_FORMAT.getMessage(), stockCount);
    }

    public void printRequestOrder() {
        System.out.println();
        System.out.println(OutputMessage.ORDER_REQUEST_MESSAGE.getMessage());
    }

    public void printSuggestPromotionBenefit(final String productName) {
        System.out.println();
        System.out.printf(OutputMessage.PROMOTION_BENEFIT_SUGGESTION_FORMAT.getMessage(), productName);
    }

    public void printFullPriceQuantityNotification(final String productName, final int quantity) {
        System.out.println();
        System.out.printf(OutputMessage.FULL_PRICE_QUANTITY_NOTIFICATION_FORMAT.getMessage(), productName, quantity);
    }

    public void printConfirmMembershipDiscount() {
        System.out.println();
        System.out.println(OutputMessage.MEMBERSHIP_DISCOUNT_SUGGESTION.getMessage());
    }

    public void printReceipt(final Receipt receipt) {
        StringBuilder receiptContents = new StringBuilder("\n")
                .append("==============W 편의점================\n")
                .append(ReceiptFormat.PRODUCT_ITEM_HEADER.getFormat())
                .append(buildProductItems(receipt.entries()))
                .append("=============증\t\t정===============\n")
                .append(buildFreePromotionItems(receipt.entries()))
                .append("====================================\n")
                .append(buildPriceInformation(receipt));
        System.out.println(receiptContents);
    }

    private String buildProductItems(final List<ReceiptEntry> receiptEntries) {
        StringBuilder productItems = new StringBuilder();
        receiptEntries.forEach(entry -> {
            String format = getKoreanWidth(entry.productName(), 10) + ReceiptFormat.PRODUCT_ITEM.getFormat();
            String itemContents = String.format(format, entry.productName(), entry.quantity(), entry.price());
            productItems.append(itemContents);
        });
        return productItems.toString();
    }

    private String buildFreePromotionItems(final List<ReceiptEntry> receiptEntries) {
        StringBuilder freePromotionItems = new StringBuilder();
        receiptEntries.stream()
                .filter(entry -> entry.freeQuantity() > 0)
                .forEach(entry -> {
                    String format = getKoreanWidth(entry.productName(), 10) + ReceiptFormat.PROMOTION_ITEM.getFormat();
                    String itemContents = String.format(format, entry.productName(), entry.freeQuantity());
                    freePromotionItems.append(itemContents);
                });
        return freePromotionItems.toString();
    }

    private String getKoreanWidth(final String text, final int targetWidth) {
        int length = targetWidth - text.length();
        return "%-" + length + "s";
    }

    private String buildPriceInformation(final Receipt receipt) {
        StringBuilder priceInformation = new StringBuilder()
                .append(String.format(ReceiptFormat.TOTAL_PRICE.getFormat(), "총구매액", receipt.totalQuantity(),
                        receipt.totalPrice()))
                .append(String.format(ReceiptFormat.PROMOTION_DISCOUNT.getFormat(), "행사할인",
                        receipt.promotionDiscount()))
                .append(String.format(ReceiptFormat.MEMBERSHIP_DISCOUNT.getFormat(), "멤버십할인",
                        receipt.memberShipDiscount()))
                .append(String.format(ReceiptFormat.PAID_AMOUNT.getFormat(), "내실돈", receipt.getPaidAmount()));
        return priceInformation.toString();
    }

    public void printSuggestReorder() {
        System.out.println();
        System.out.println(OutputMessage.REORDER_SUGGESTION.getMessage());
    }

    public void printMessage(final String message) {
        System.out.println(message);
    }
}
