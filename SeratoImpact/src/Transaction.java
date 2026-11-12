public class Transaction {
    private String transactionID;
    private double totalAmount;
    private String status;

    public void complete() {
        this.status = "Completed";
    }

    public void cancel() {
        this.status = "Cancelled";
    }
}
