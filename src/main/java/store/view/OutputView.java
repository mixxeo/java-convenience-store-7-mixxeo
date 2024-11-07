package store.view;

import java.util.List;
import java.util.stream.Collectors;
import store.dto.ProductInformation;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String PRODUCT_FORMAT = "- %s %,d원 %s";
    private static final String PROMOTION_PRODUCT_FORMAT = PRODUCT_FORMAT + " %s";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String PRODUCT_STOCK_COUNT_FORMAT = "%,d개";
    private static final String ORDER_REQUEST_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public void printProductCatalog(List<ProductInformation> productInformation) {
        printWelcomeMessage();
        String catalog = productInformation.stream()
                .map(this::buildProductMessage)
                .collect(Collectors.joining("\n"));
        System.out.println(catalog);
    }

    private void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    private String buildProductMessage(ProductInformation productInformation) {
        String name = productInformation.name();
        int price = productInformation.price();
        String stockCount = formatProductStockCount(productInformation.stockCount());
        String promotionName = productInformation.promotionName();

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

    public void printMessage(String message) {
        System.out.println(message);
    }
}
