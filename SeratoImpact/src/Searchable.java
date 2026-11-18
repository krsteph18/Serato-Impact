import java.util.List;

public interface Searchable {
    List<InventoryItem> searchCards(String keyword);
}
