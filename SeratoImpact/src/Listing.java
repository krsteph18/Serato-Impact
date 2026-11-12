public class Listing {
    private String listingID;
    private double price;
    private String status;
    private int availableQuantity;

    public Listing(String listingID, double price, String status, int availableQuantity) {
        this.listingID = listingID;
        this.price = price;
        this.status = status;
        this.availableQuantity = availableQuantity;
    }

    public String getListingID() {
        return listingID;
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
        this.listingID = listingID;
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

    }
    public boolean isAvailable() {
        return false;
    }
    public void setAvailability(boolean available) {

    }
}
