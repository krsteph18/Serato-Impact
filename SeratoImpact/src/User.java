import java.io.Serializable;
import java.util.UUID;

/** Local seller profile (not an authenticated online account). */
public final class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String userId;
    private String displayName;
    private String contact;
    public User(String displayName, String contact) {
        this.userId = UUID.randomUUID().toString();
        update(displayName, contact);
    }
    public void update(String displayName, String contact) {
        if (displayName == null || displayName.trim().isEmpty())
            throw new IllegalArgumentException("Seller name is required.");
        this.displayName = displayName.trim();
        this.contact = contact == null ? "" : contact.trim();
    }
    public String getUserId() { return userId; }
    public String getDisplayName() { return displayName; }
    public String getContact() { return contact; }
    @Override public String toString() { return displayName; }
}
