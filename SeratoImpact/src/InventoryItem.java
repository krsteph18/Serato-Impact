import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/** One physical card type/copy group in one binder. */
public final class InventoryItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String itemId;
    private final Card card;
    private String condition;
    private int quantity;
    private Listing listing;
    public InventoryItem(Card card, String condition, int quantity, BigDecimal price, boolean forSale) {
        this.itemId = UUID.randomUUID().toString();
        if (card == null) throw new IllegalArgumentException("Card is required.");
        this.card = card;
        update(condition, quantity, price, forSale);
    }
    public void update(String condition, int quantity, BigDecimal price, boolean forSale) {
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
        if (forSale && quantity < 1) throw new IllegalArgumentException("For-sale stock must be positive.");
        if (listing == null) listing = new Listing(price, forSale);
        else listing.update(price, forSale);
        this.condition = condition == null ? "" : condition.trim();
        this.quantity = quantity;
    }
    public String getItemId() { return itemId; }
    public Card getCard() { return card; }
    public String getCondition() { return condition; }
    public int getQuantity() { return quantity; }
    public Listing getListing() { return listing; }
    public boolean isAvailableForSale() { return listing != null && listing.isForSale() && quantity > 0; }
}
