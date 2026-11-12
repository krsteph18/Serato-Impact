public class Listing {
    private String name;
    private double price;
    private String status;
    private int availableQuantity;

    public Listing(String listingID, double price, String status, int availableQuantity) {
        this.name = listingID;
        this.price = price;
        this.status = status;
        this.availableQuantity = availableQuantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setListingID(String listingID) {
        this.name = listingID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
    public void updatePrice(double price) {
        if (price < 0) {
            return;
        }
        this.price = price;
    }
    public boolean isAvailable() {
        return false;
    }
    public void setAvailability(boolean available) {
        if (available) {
            this.availableQuantity += 1;
        } else {
            this.availableQuantity = 0;
        }
    }
}
