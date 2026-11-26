
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public final class BinderPagesForm {

    private JPanel pagesMainPanel;
    private JLabel lblBinderName;

    private JPanel cardsPanel;

    private JButton btnCard1, btnCard2, btnCard3;
    private JButton btnCard4, btnCard5, btnCard6;
    private JButton btnCard7, btnCard8, btnCard9;

    private JPanel navigationPanel;
    private JButton btnBack, btnNextPage;
    private JLabel lblPageNumber;

    private JPanel pagesToolbar;
    private JButton btnAddCard, btnBackToBinders;

    private Binder binder;
    private boolean sellerMode;
    private int currentPage;

    private final Consumer<InventoryItem> onOpenCard;
    private final Runnable onBackToCover;

    public BinderPagesForm(
            Runnable onBackToCover,
            Runnable onBackToBinders,
            Runnable onAdd,
            Consumer<InventoryItem> onOpenCard
    ) {

        this.onBackToCover = onBackToCover;
        this.onOpenCard = onOpenCard;

        requireForm();

        btnNextPage.addActionListener(e -> {
            if (currentPage < getTotalPages() - 1) {
                currentPage++;
                displayPage();
            }
        });

        btnBack.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage();
            } else {
                onBackToCover.run();
            }
        });

        btnBackToBinders.addActionListener(e ->
                onBackToBinders.run()
        );

        btnAddCard.addActionListener(e ->
                onAdd.run()
        );
    }

    public void setBinder(Binder binder, boolean sellerMode) {

        this.binder = binder;
        this.sellerMode = sellerMode;
        currentPage = 0;

        btnAddCard.setVisible(sellerMode);

        displayPage();
    }

    public void refresh() {
        displayPage();
    }

    private int getTotalPages() {
        return binder == null ? 1 : binder.getTotalPages(sellerMode);
    }

    private void displayPage() {

        if (binder == null) return;

        List<InventoryItem> cards =
                binder.getVisibleItems(sellerMode);

        lblBinderName.setText(
                binder.getName() + " - " +
                        (sellerMode ? "MY COLLECTION" : "FOR SALE")
        );

        if (currentPage >= getTotalPages()) {
            currentPage = getTotalPages() - 1;
        }

        cardsPanel.removeAll();

        cardsPanel.setLayout(
                new GridLayout(3, 3, 12, 12)
        );

        for (int i = 0; i < 9; i++) {

            int index = currentPage * 9 + i;

            // Main pocket container
            JPanel cardContainer = new JPanel(new BorderLayout(0, 3));

            cardContainer.setBackground(Color.WHITE);

            cardContainer.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(
                                    new Color(210, 210, 210), 1
                            ),
                            BorderFactory.createEmptyBorder(4, 4, 3, 4)
                    )
            );

            if (index < cards.size()) {

                InventoryItem item = cards.get(index);
                Card card = item.getCard();

                // IMAGE AREA
                JPanel imagePanel = new JPanel(new GridBagLayout());

                imagePanel.setBackground(Color.WHITE);

                // Load centered card image
                ImageIcon image = CardImage.load(
                        card.getImagePath(), 125, 155
                );

                JButton imageButton = new JButton();

                if (image != null) {
                    imageButton.setIcon(image);
                } else {
                    imageButton.setText("NO IMAGE");
                }

                imageButton.setPreferredSize(new Dimension(125, 155));
                imageButton.setBorder(null);
                imageButton.setContentAreaFilled(false);
                imageButton.setFocusPainted(false);
                imageButton.setMargin(new Insets(0, 0, 0, 0));
                imageButton.setOpaque(false);

                // Open card details when clicked
                imageButton.addActionListener(e ->
                        onOpenCard.accept(item)
                );

                imagePanel.add(imageButton);

                cardContainer.add(
                        imagePanel, BorderLayout.CENTER
                );

                // SMALL DETAILS AREA AT THE BOTTOM
                JPanel details = new JPanel(
                        new GridLayout(2, 1, 0, 0)
                );

                details.setBackground(Color.WHITE);

                details.setPreferredSize(
                        new Dimension(100, 34)
                );

                // Card name
                JLabel name = new JLabel(
                        card.getName(),
                        SwingConstants.CENTER
                );

                name.setFont(
                        new Font("Arial", Font.BOLD, 11)
                );

                name.setForeground(Color.BLACK);

                // Card price
                String price = "";

                if (item.isAvailableForSale()
                        && item.getListing() != null
                        && item.getListing().getPrice() != null) {

                    price = "PHP " +
                            item.getListing().getPrice().toPlainString();
                }

                JLabel priceLabel = new JLabel(
                        price,
                        SwingConstants.CENTER
                );

                priceLabel.setFont(
                        new Font("Arial", Font.PLAIN, 10)
                );

                priceLabel.setForeground(
                        new Color(90, 90, 90)
                );

                details.add(name);
                details.add(priceLabel);

                cardContainer.add(
                        details, BorderLayout.SOUTH
                );

                cardContainer.setToolTipText(card.getName());

            } else {

                // EMPTY POCKET
                JLabel emptyLabel = new JLabel(
                        "EMPTY POCKET",
                        SwingConstants.CENTER
                );

                emptyLabel.setFont(
                        new Font("Arial", Font.PLAIN, 11)
                );

                emptyLabel.setForeground(
                        new Color(140, 140, 140)
                );

                cardContainer.add(
                        emptyLabel, BorderLayout.CENTER
                );
            }

            cardsPanel.add(cardContainer);
        }

        // PAGE INFORMATION
        lblPageNumber.setText(
                "Page " + (currentPage + 1) +
                        " of " + getTotalPages()
        );

        btnNextPage.setEnabled(
                currentPage < getTotalPages() - 1
        );

        btnBack.setText(
                currentPage == 0
                        ? "BACK TO COVER"
                        : "PREVIOUS PAGE"
        );

        cardsPanel.revalidate();
        cardsPanel.repaint();

        pagesMainPanel.revalidate();
        pagesMainPanel.repaint();
    }

    public JPanel getMainPanel() {
        return pagesMainPanel;
    }

    private void requireForm() {

        if (pagesMainPanel == null ||
                lblBinderName == null ||
                cardsPanel == null ||
                btnCard1 == null ||
                btnCard2 == null ||
                btnCard3 == null ||
                btnCard4 == null ||
                btnCard5 == null ||
                btnCard6 == null ||
                btnCard7 == null ||
                btnCard8 == null ||
                btnCard9 == null ||
                navigationPanel == null ||
                btnBack == null ||
                btnNextPage == null ||
                lblPageNumber == null ||
                pagesToolbar == null ||
                btnAddCard == null ||
                btnBackToBinders == null) {

            throw new IllegalStateException(
                    "BinderPagesForm.form was not initialized. " +
                            "Build with IntelliJ Swing UI Designer (binary class files)."
            );
        }
    }
}
