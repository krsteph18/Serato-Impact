public final class CartItem {
    private final String binderId;
    private final String inventoryItemId;
    private int quantity;

    CartItem(String binderId, String inventoryItemId, int quantity) {
        this.binderId = binderId;
        this.inventoryItemId = inventoryItemId;
        this.quantity = quantity;
    }

    public String getBinderId() {
        return binderId;
    }

    public String getInventoryItemId() {
        return inventoryItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
