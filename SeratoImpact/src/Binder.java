import java.util.*;
public class Binder {
    private String binderID;
    private String name;
    private boolean isPublic;
    private List<InventoryItem> items;

    public void addCard(String itemId) {
        InventoryItem item = new InventoryItem(itemId);
        items.add(item);
    }

    public void addCard(String itemId, String condition) {
        InventoryItem item = new InventoryItem(itemId, condition);
        items.add(item);
    }

    public void removeCard(String itemId) {
        for (InventoryItem item : items) {
            if (item.getItemId() == itemId) {
                items.remove(item);
            }
        }
    }

    public List<InventoryItem> viewCards() {
        return items;
    }
}
