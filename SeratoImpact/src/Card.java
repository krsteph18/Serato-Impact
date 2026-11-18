import java.io.Serializable;
import java.util.UUID;

/** Descriptive information for a collectible card; no game mechanics. */
public final class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String cardId;
    private String name, category, setName, rarity, imagePath;

    public Card(String name, String category, String setName, String rarity, String imagePath) {
        this.cardId = UUID.randomUUID().toString();
        update(name, category, setName, rarity, imagePath);
    }
    public void update(String name, String category, String setName, String rarity, String imagePath) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Card name is required.");
        this.name = name.trim();
        this.category = safe(category);
        this.setName = safe(setName);
        this.rarity = safe(rarity);
        this.imagePath = safe(imagePath);
    }
    private static String safe(String s) { return s == null ? "" : s.trim(); }
    public String getCardId() { return cardId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSetName() { return setName; }
    public String getRarity() { return rarity; }
    public String getImagePath() { return imagePath; }
}
