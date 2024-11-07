package store.view;

import java.util.List;
import java.util.stream.Collectors;
import store.dto.ProductInformation;

public class OutputView {
    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private static final String PRODUCT_FORMAT = "- %s %,d원 %s";
    private static final String PROMOTION_PRODUCT_FORMAT = PRODUCT_FORMAT + " %s";
    private static final String OUT_OF_STOCK = "재고 없음";
    private static final String PRODUCT_QUANTITY_FORMAT = "%,d개";

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
        String quantity = formatProductQuantity(productInformation.quantity());
        String promotionName = productInformation.promotionName();

        if (promotionName == null) {
            return String.format(PRODUCT_FORMAT, name, price, quantity);
        }
        return String.format(PROMOTION_PRODUCT_FORMAT, name, price, quantity, promotionName);
    }

    private static String formatProductQuantity(int quantity) {
        if (quantity == 0) {
            return OUT_OF_STOCK;
        }
        return String.format(PRODUCT_QUANTITY_FORMAT, quantity);
    }
}
