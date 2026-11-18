import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Application data model and public inventory search; no reservation or checkout. */
public final class Marketplace implements Searchable, Serializable {
    private static final long serialVersionUID = 1L;
    private final List<User> sellers = new ArrayList<>();
    private final List<Binder> binders = new ArrayList<>();
    public Marketplace() {
        User starter = addSeller("Sample Seller", "Add your contact in Seller Profile");
        addBinder(starter, "Character Cards", "Character card collection", true);
        addBinder(starter, "Equipment Cards", "Equipment card collection", true);
        addBinder(starter, "Event Cards", "Event card collection", true);
        addBinder(starter, "Support Cards", "Support card collection", true);
    }
    public User addSeller(String name, String contact) {
        User seller = new User(name, contact); sellers.add(seller); return seller;
    }
    public Binder addBinder(User seller, String name, String description, boolean isPublic) {
        if (seller == null || !sellers.contains(seller)) throw new IllegalArgumentException("Unknown seller.");
        Binder binder = new Binder(seller.getUserId(), name, description, isPublic);
        binders.add(binder); return binder;
    }
    public void removeBinder(Binder binder) { binders.remove(binder); }
    public List<User> getSellers() { return Collections.unmodifiableList(sellers); }
    public List<Binder> getBinders() { return Collections.unmodifiableList(binders); }
    public User getOwner(Binder binder) {
        for (User user : sellers) if (user.getUserId().equals(binder.getOwnerId())) return user;
        return null;
    }
    public List<Binder> bindersFor(User seller) {
        List<Binder> result = new ArrayList<>();
        if (seller != null) for (Binder b : binders) if (b.getOwnerId().equals(seller.getUserId())) result.add(b);
        return result;
    }
    public List<Binder> publicBinders() {
        List<Binder> result = new ArrayList<>();
        for (Binder b : binders) if (b.isPublic()) result.add(b);
        return result;
    }
    @Override public List<InventoryItem> searchCards(String keyword) {
        String search = keyword == null ? "" : keyword.toLowerCase(Locale.ROOT).trim();
        List<InventoryItem> result = new ArrayList<>();
        for (Binder binder : publicBinders()) for (InventoryItem item : binder.getVisibleItems(false)) {
            Card c = item.getCard();
            if (contains(c.getName(), search) || contains(c.getCategory(), search)
                    || contains(c.getSetName(), search) || contains(c.getRarity(), search)
                    || contains(binder.getName(), search)) result.add(item);
        }
        return result;
    }
    public static boolean contains(String value, String search) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(search);
    }
}
