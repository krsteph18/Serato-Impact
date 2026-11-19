import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
public final class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String orderId, sellerId, buyerName, buyerContact;
    private final LocalDateTime createdAt;
    private final List<OrderItem> items;
    private final BigDecimal total;
    private OrderStatus status = OrderStatus.AWAITING_PICKUP;

    public Order(String sellerId, String orderId, String buyerName, String buyerContact, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain items.");
        }

        this.orderId = UUID.randomUUID().toString();
        this.sellerId = sellerId;
        this.buyerName = buyerName;
        this.buyerContact = buyerContact;
        this.createdAt = LocalDateTime.now();
        this.items = List.copyOf(items);
        BigDecimal t = BigDecimal.ZERO;
        for (OrderItem item : items) {
            t = t.add(item.getSubtotal());
        }
        total = t;
    }

    public String getOrderId() {
        return orderId;
    }
    public String getShortId() {
        return orderId.substring(0, 8).toUpperCase(Locale.ROOT);
    }
    public String getSellerId() {
        return sellerId;
    }
    public String getBuyerName() {
        return buyerName;
    }
    public String getBuyerContact() {
        return buyerContact;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public OrderStatus getStatus() {
        return status;
    }

    public void markCollected() {
        if (status != OrderStatus.AWAITING_PICKUP) {
            throw new IllegalArgumentException("Only pending orders may be completed.");
        }
        status = OrderStatus.COMPLETED;
    }

    void cancel() {
        if (status != OrderStatus.AWAITING_PICKUP) {
            throw new IllegalArgumentException("Only pending orders may be completed.");
        }
        status = OrderStatus.CANCELLED;
    }
}
