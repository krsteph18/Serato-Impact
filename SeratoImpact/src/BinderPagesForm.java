import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public final class BinderPagesForm {
    private JPanel pagesMainPanel;
    private JLabel lblBinderName;
    private JPanel cardsPanel;
    private JButton btnCard1,btnCard2,btnCard3,btnCard4,btnCard5,btnCard6,btnCard7,btnCard8,btnCard9;
    private JPanel navigationPanel;
    private JButton btnBack,btnNextPage;
    private JLabel lblPageNumber;
    private JPanel pagesToolbar;
    private JButton btnAddCard,btnBackToBinders;
    private JButton[] cardButtons;
    private Binder binder;
    private boolean sellerMode;
    private int currentPage;
    private final Consumer<InventoryItem> onOpenCard;
    private final Runnable onBackToCover;

    public BinderPagesForm(Runnable onBackToCover,Runnable onBackToBinders,Runnable onAdd,Consumer<InventoryItem> onOpenCard) {
        this.onBackToCover=onBackToCover;
        this.onOpenCard=onOpenCard;
        requireForm();
        cardButtons=new JButton[]{btnCard1,btnCard2,btnCard3,btnCard4,btnCard5,btnCard6,btnCard7,btnCard8,btnCard9};
        for (JButton b : cardButtons) {
            b.addActionListener(e->{
                if (binder==null) return;
                int i=currentPage*9;
                for (int j=0;j<cardButtons.length;j++) if (cardButtons[j]==b) i+=j;
                List<InventoryItem> cards=binder.getVisibleItems(sellerMode);
                if (i<cards.size()) onOpenCard.accept(cards.get(i));
            });
        }
        btnNextPage.addActionListener(e->{if (currentPage<getTotalPages()-1){currentPage++;displayPage();}});
        btnBack.addActionListener(e->{if(currentPage>0){currentPage--;displayPage();}else onBackToCover.run();});
        btnBackToBinders.addActionListener(e->onBackToBinders.run());
        btnAddCard.addActionListener(e->onAdd.run());
    }

    public void setBinder(Binder binder,boolean sellerMode){
        this.binder=binder;this.sellerMode=sellerMode;currentPage=0;
        btnAddCard.setVisible(sellerMode);
        displayPage();
    }
    public void refresh(){displayPage();}
    private int getTotalPages(){return binder==null?1:binder.getTotalPages(sellerMode);}
    private void displayPage() {
        if (binder == null) return;

        List<InventoryItem> cards = binder.getVisibleItems(sellerMode);

        lblBinderName.setText(
                binder.getName() + " - " +
                        (sellerMode ? "MY COLLECTION" : "FOR SALE")
        );

        if (currentPage >= getTotalPages()) {
            currentPage = getTotalPages() - 1;
        }

        cardsPanel.removeAll();
        cardsPanel.setLayout(new GridLayout(3, 3, 12, 12));

        for (int i = 0; i < 9; i++) {
            int index = currentPage * 9 + i;

            JPanel cardContainer = new JPanel(new BorderLayout(8, 0));
            cardContainer.setBackground(Color.WHITE);
            cardContainer.setBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            );

            if (index < cards.size()) {

                InventoryItem item = cards.get(index);
                Card card = item.getCard();

                // Card image on the left
                ImageIcon image = CardImage.loadCover(
                        card.getImagePath(), 140, 240
                );

                JButton imageButton = new JButton();
                imageButton.setIcon(image);
                imageButton.setPreferredSize(new Dimension(140, 240));
                imageButton.setBorder(null);
                imageButton.setContentAreaFilled(false);
                imageButton.setFocusPainted(false);
                imageButton.setMargin(new Insets(0, 0, 0, 0));

                imageButton.addActionListener(e ->
                        onOpenCard.accept(item)
                );

                cardContainer.add(imageButton, BorderLayout.WEST);

                // Card details on the right
                JPanel details = new JPanel();
                details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
                details.setBackground(Color.WHITE);

                JLabel name = new JLabel(card.getName());
                name.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel category = new JLabel(card.getCategory());
                JLabel set = new JLabel("Set: " + card.getSetName());
                JLabel rarity = new JLabel("Rarity: " + card.getRarity());

                String price = item.isAvailableForSale()
                        ? "PHP " + item.getListing().getPrice().toPlainString()
                        : "NOT FOR SALE";

                JLabel priceLabel = new JLabel(price);
                JLabel quantity = new JLabel("Qty: " + item.getQuantity());

                details.add(name);
                details.add(Box.createVerticalStrut(8));
                details.add(category);
                details.add(Box.createVerticalStrut(5));
                details.add(set);
                details.add(Box.createVerticalStrut(5));
                details.add(rarity);
                details.add(Box.createVerticalStrut(12));
                details.add(priceLabel);
                details.add(quantity);

                cardContainer.add(details, BorderLayout.CENTER);

                cardContainer.setToolTipText(card.getName());

            } else {
                cardContainer.add(
                        new JLabel("EMPTY POCKET", SwingConstants.CENTER),
                        BorderLayout.CENTER
                );
            }

            cardsPanel.add(cardContainer);
        }

        lblPageNumber.setText(
                "Page " + (currentPage + 1) + " of " + getTotalPages()
        );

        btnNextPage.setEnabled(currentPage < getTotalPages() - 1);

        btnBack.setText(
                currentPage == 0 ? "BACK TO COVER" : "PREVIOUS PAGE"
        );

        cardsPanel.revalidate();
        cardsPanel.repaint();

        pagesMainPanel.revalidate();
        pagesMainPanel.repaint();
    }
    public JPanel getMainPanel(){return pagesMainPanel;}

    private void requireForm() {
        if (pagesMainPanel == null || lblBinderName == null || cardsPanel == null || btnCard1 == null || btnCard2 == null || btnCard3 == null || btnCard4 == null || btnCard5 == null || btnCard6 == null || btnCard7 == null || btnCard8 == null || btnCard9 == null || navigationPanel == null || btnBack == null || btnNextPage == null || lblPageNumber == null || pagesToolbar == null || btnAddCard == null || btnBackToBinders == null) {
            throw new IllegalStateException("BinderPagesForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
    }
}
