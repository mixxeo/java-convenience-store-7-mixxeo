package store.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constant.ExceptionMessage;
import store.dto.Receipt;
import store.dto.ReceiptEntry;
import store.model.Order;
import store.model.OrderItem;
import store.model.ProductManager;
import store.model.Quantity;

public class OrderService {
    private static final Pattern ORDER_ITEM_PATTERN = Pattern.compile("^\\[(.+)-(\\d+)]$");
    private static final int PRODUCT_NAME_GROUP_INDEX = 1;
    private static final int QUANTITY_GROUP_INDEX = 2;

    public Order createOrder(List<String> items, ProductManager productManager) {
        List<OrderItem> orderItems = items.stream()
                .map(item -> createOrderItem(item, productManager))
                .toList();
        return new Order(orderItems);
    }

    private OrderItem createOrderItem(String item, ProductManager productManager) {
        Matcher matcher = ORDER_ITEM_PATTERN.matcher(item);
        validateOrderFormat(matcher);

        String productName = matcher.group(PRODUCT_NAME_GROUP_INDEX);
        Quantity quantity = Quantity.from(matcher.group(QUANTITY_GROUP_INDEX));
        return OrderItem.of(productName, quantity, productManager);
    }

    private void validateOrderFormat(Matcher matcher) {
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ExceptionMessage.ORDER_INVALID_FORMAT.getMessage());
        }
    }

    public Receipt generateReceipt(Order order, boolean isMembership) {
        List<ReceiptEntry> receiptEntries = order.items().stream()
                .map(ReceiptEntry::from)
                .toList();
        return Receipt.of(receiptEntries, order, isMembership);
    }
}
