import java.io.Serializable;
import java.math.BigDecimal;

/** An optional sale offering for a specific physical inventory item. */
public final class Listing implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal price;
    private boolean forSale;
    public Listing(BigDecimal price, boolean forSale) { update(price, forSale); }
    public void update(BigDecimal price, boolean forSale) {
        if (price == null || price.signum() < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        if (forSale && price.signum() == 0)
            throw new IllegalArgumentException("For-sale cards must have a price greater than zero.");
        this.price = price;
        this.forSale = forSale;
    }
    public BigDecimal getPrice() { return price; }
    public boolean isForSale() { return forSale; }
}
