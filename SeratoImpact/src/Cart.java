import java.util.*;
public class Cart {
    private final Map<String, CartItem> items = new LinkedHashMap<>();

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    public int totalQuantity() {
        int total = 0;

        for (CartItem item : items.values()) {
            total += item.getQuantity();
        }

        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(Binder binder, InventoryItem item, int quantity) {
        if (binder == null || item == null || !binder.isPublic() || !binder.getItems().contains(item) || !item.isAvailableForSale()) {
            throw new IllegalArgumentException("This card is not currently available for sale.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        CartItem existing = items.get(item.getItemId());

        long requested = (long) quantity + (existing == null ? 0 : existing.getQuantity());

        if (requested > item.getQuantity()) {
            throw new IllegalArgumentException("Only " + item.getQuantity() + " available.");
        }

        if (existing == null) {
            items.put(item.getItemId(), new CartItem(binder.getBinderId(), item.getItemId(), quantity));
        } else {
            existing.setQuantity((int)requested);
        }
    }

    public void changeQuantity(CartItem entry, int quantity) {
        if (entry == null || !items.containsKey(entry.getInventoryItemId())) {
            return;
        }

        if (quantity <= 0) {
            items.remove(entry.getInventoryItemId());
        } else {
            items.get(entry.getInventoryItemId()).setQuantity(quantity);
        }
    }

    public void remove(CartItem entry) {
        if (entry != null) {
            items.remove(entry.getInventoryItemId());
        }
    }

    public void clear() {
        items.clear();
    }
}
