import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BinderPagesForm {
    private JPanel pagesMainPanel;
    private JLabel lblBinderName;
    private JPanel cardsPanel;
    private JButton btnCard1;
    private JButton btnCard2;
    private JButton btnCard3;
    private JButton btnCard4;
    private JButton btnCard5;
    private JButton btnCard6;
    private JButton btnCard7;
    private JButton btnCard8;
    private JButton btnCard9;
    private JPanel navigationPanel;
    private JButton btnBack;
    private JButton btnNextPage;
    private JLabel lblPageNumber;
    private JButton[] cardButtons;

    private List<String> cards = new ArrayList<>();

    private int currentPage = 0;
    private final int CARDS_PER_PAGE = 9;

    private Runnable onBackToCover;

    public BinderPagesForm(Runnable onBackToCover) {

        this.onBackToCover = onBackToCover;

        cardButtons = new JButton[] {
                btnCard1, btnCard2, btnCard3,
                btnCard4, btnCard5, btnCard6,
                btnCard7, btnCard8, btnCard9
        };

        // Next page
        btnNextPage.addActionListener(e -> {
            if (currentPage < getTotalPages() - 1) {
                currentPage++;
                displayPage();
            }
        });

        // Previous page or cover
        btnBack.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage();
            } else {
                onBackToCover.run();
            }
        });

        // Clicking a card
        for (JButton button : cardButtons) {
            button.addActionListener(e -> {
                JOptionPane.showMessageDialog(
                        pagesMainPanel,
                        button.getText(),
                        "Card Details",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });
        }
    }

    // Load selected binder
    public void setBinder(String binderType) {

        lblBinderName.setText(
                binderType.toUpperCase() + " CARD"
        );

        cards.clear();

        // Temporary sample cards
        for (int i = 1; i <= 28; i++) {
            cards.add(binderType + " Card " + i);
        }

        currentPage = 0;
        displayPage();
    }

    // Calculate total pages
    private int getTotalPages() {

        return Math.max(1,
                (cards.size() + CARDS_PER_PAGE - 1)
                        / CARDS_PER_PAGE);
    }

    // Update displayed cards
    private void displayPage() {

        int start = currentPage * CARDS_PER_PAGE;

        for (int i = 0; i < cardButtons.length; i++) {

            int cardIndex = start + i;

            if (cardIndex < cards.size()) {

                cardButtons[i].setText(
                        cards.get(cardIndex)
                );

                cardButtons[i].setEnabled(true);

            } else {

                cardButtons[i].setText("");
                cardButtons[i].setEnabled(false);
            }
        }

        lblPageNumber.setText(
                "Page " + (currentPage + 1)
                        + " of " + getTotalPages()
        );

        btnNextPage.setEnabled(
                currentPage < getTotalPages() - 1
        );

        if (currentPage == 0) {
            btnBack.setText("Back to Cover");
        } else {
            btnBack.setText("Previous Page");
        }
    }

    public JPanel getMainPanel() {
        return pagesMainPanel;
    }
}