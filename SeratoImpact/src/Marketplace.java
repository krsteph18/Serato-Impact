import java.util.ArrayList;
import java.util.List;

public class Marketplace implements Searchable{
    List<Listing> listings = new ArrayList<>();

    @Override
    public List<Listing> searchCards(String name) {
        List<Listing> keyCards = new ArrayList<>();
        for (Listing l : listings) {
            if (l.getName().equals(name)) {
                keyCards.add(l);
            }
        }
        return keyCards;
    }

    public List<Listing> viewAvailableCards() {
        List<Listing> availableListings = new ArrayList<>();
        for (Listing l : listings) {
            if (l.isAvailable()) {
                availableListings.add(l);
            }
        }
        return availableListings;
    }
}
