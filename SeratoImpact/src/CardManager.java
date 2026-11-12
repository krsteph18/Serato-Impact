import java.util.*;

public class CardManager {
    private List<Card> cardCatalog = new ArrayList<>();

    public void addCard(Card c) {
        cardCatalog.add(c);
    }
    public void removeCard(Card c) {
        for (Card card : cardCatalog) {
            if (card == c) {
                cardCatalog.remove(card);
            }
        }
    }
    public Card findCard(String name) {
        for (Card card : cardCatalog) {
            if (card.getName() == name) {
                return card;
            }
        }
        return null;
    }
}
