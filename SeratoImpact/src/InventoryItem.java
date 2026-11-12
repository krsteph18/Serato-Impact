public class InventoryItem {
    private String itemId;
    private String condition;
    private int quantity;

    public InventoryItem(String itemId) {
        this.itemId = itemId;
    }
    public InventoryItem(String itemId, String condition) {
        this(itemId);
        this.condition = condition;
    }

    public String getItemId() {
        return itemId;
    }

    public String getCondition() {
        return condition;
    }

    public void updateQuantity(int quantity) {

    }
}
