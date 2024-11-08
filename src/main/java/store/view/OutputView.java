package store.view;

import java.util.List;
import java.util.stream.Collectors;
import store.dto.CatalogEntry;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String PRODUCT_FORMAT = "- %s %,d원 %s";
    private static final String PROMOTION_PRODUCT_FORMAT = PRODUCT_FORMAT + " %s";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String PRODUCT_STOCK_COUNT_FORMAT = "%,d개";
    private static final String ORDER_REQUEST_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String FREE_PRODUCT_OFFER_FORMAT
            = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n";
    private static final String FULL_PRICE_QUANTITY_NOTIFICATION_FORMAT
            = "현재 %s %,d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n";

    public void printProductCatalog(List<CatalogEntry> productInformation) {
        printWelcomeMessage();
        String catalog = productInformation.stream()
                .map(this::buildProductMessage)
                .collect(Collectors.joining("\n"));
        System.out.println(catalog);
    }

    private void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    private String buildProductMessage(CatalogEntry catalogEntry) {
        String name = catalogEntry.name();
        int price = catalogEntry.price();
        String stockCount = formatProductStockCount(catalogEntry.stockCount());
        String promotionName = catalogEntry.promotionName();

        if (promotionName == null) {
            return String.format(PRODUCT_FORMAT, name, price, stockCount);
        }
        return String.format(PROMOTION_PRODUCT_FORMAT, name, price, stockCount, promotionName);
    }

    private String formatProductStockCount(int stockCount) {
        if (stockCount == 0) {
            return OUT_OF_STOCK;
        }
        return String.format(PRODUCT_STOCK_COUNT_FORMAT, stockCount);
    }

    public void printRequestOrder() {
        System.out.println();
        System.out.println(ORDER_REQUEST_MESSAGE);
    }

    public void printOfferFreeProduct(String productName) {
        System.out.println();
        System.out.printf(FREE_PRODUCT_OFFER_FORMAT, productName);
    }

    public void printFullPriceQuantityNotification(String productName, int quantity) {
        System.out.println();
        System.out.printf(FULL_PRICE_QUANTITY_NOTIFICATION_FORMAT, productName, quantity);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
