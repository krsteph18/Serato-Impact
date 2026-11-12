import java.util.*;
public class Binder {
    private String binderID;
    private String name;
    private boolean isPublic;
    private List<InventoryItem> items = new ArrayList<>();

    public Binder(String binderID, String name) {
        this.binderID = binderID;
        this.name = name;
        this.isPublic = false;
    }

    public String getBinderID() {
        return binderID;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setPublic() {
        this.isPublic = true;
    }

    public void setPrivate() {
        this.isPublic = false;
    }

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
