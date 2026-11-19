import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Binder implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String binderId;
    private final String ownerId;
    private String name;
    private String description;
    private boolean isPublic;
    private final List<InventoryItem> items = new ArrayList<>();

    public Binder(String ownerId, String name, String description, boolean isPublic) {
        this.binderId = UUID.randomUUID().toString();
        this.ownerId = ownerId;
        update(name, description, isPublic);
    }
    public void update(String name, String description, boolean isPublic) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Binder name is required.");
        this.name = name.trim();
        this.description = description == null ? "" : description.trim();
        this.isPublic = isPublic;
    }
    public void addCard(InventoryItem item) {
        if (item == null) throw new IllegalArgumentException("Inventory item is required.");
        items.add(item);
    }
    public void removeCard(InventoryItem item) { items.remove(item); }
    public List<InventoryItem> getItems() { return Collections.unmodifiableList(items); }
    public int getTotalPages(boolean sellerMode) {
        int n = getVisibleItems(sellerMode).size();
        return Math.max(1, (n + 8) / 9);
    }
    public List<InventoryItem> getVisibleItems(boolean sellerMode) {
        if (sellerMode) return getItems();
        List<InventoryItem> visible = new ArrayList<>();
        if (isPublic) for (InventoryItem item : items) if (item.isAvailableForSale()) visible.add(item);
        return visible;
    }
    public String getBinderId() { return binderId; }
    public String getOwnerId() { return ownerId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isPublic() { return isPublic; }
}
