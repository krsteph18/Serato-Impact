import java.io.Serializable;
import java.math.BigDecimal;
public final class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String binderId, inventoryItemId, cardName, condition;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItem(String binderId, String inventoryItemId, String cardName, String condition, int quantity, BigDecimal unitPrice) {
        this.binderId = binderId;
        this.inventoryItemId = inventoryItemId;
        this.cardName = cardName;
        this.condition = condition;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getBinderId() {
        return binderId;
    }
    public String getInventoryItemId() {
        return inventoryItemId;
    }
    public String getCardName() {
        return cardName;
    }
    public String getCondition() {
        return condition;
    }
    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
