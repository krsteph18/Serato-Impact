
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public final class BinderPagesForm {
    private static final int POCKET_WIDTH = 110;
    private static final int CARD_DISPLAY_HEIGHT = 189;
    private static final int FOOTER_HEIGHT = 24;
    private static final int POCKET_HEIGHT =
            CARD_DISPLAY_HEIGHT + FOOTER_HEIGHT;
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
        if (binder == null) return 1;

        int count = binder.getVisibleItems(sellerMode).size();

        return Math.max(1, (count + 14) / 15);
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

        cardsPanel.setLayout(new GridBagLayout());

        for (int i = 0; i < 15; i++) {

            int index = currentPage * 15 + i;

            JPanel cardContainer = new JPanel(new BorderLayout(0, 0));

            cardContainer.setPreferredSize(
                    new Dimension(POCKET_WIDTH, POCKET_HEIGHT)
            );

            cardContainer.setMinimumSize(
                    new Dimension(POCKET_WIDTH, POCKET_HEIGHT)
            );

            cardContainer.setMaximumSize(
                    new Dimension(POCKET_WIDTH, POCKET_HEIGHT)
            );

            cardContainer.setBackground(Color.WHITE);

            cardContainer.setBorder(null);


            JPanel imagePanel = new JPanel(new GridBagLayout());

            Dimension imageAreaSize = new Dimension(
                    POCKET_WIDTH, CARD_DISPLAY_HEIGHT
            );

            imagePanel.setPreferredSize(imageAreaSize);
            imagePanel.setMinimumSize(imageAreaSize);
            imagePanel.setMaximumSize(imageAreaSize);
            imagePanel.setBackground(Color.WHITE);

            // Fixed-size footer for name and price
            JPanel details = new JPanel(new GridLayout(2, 1, 0, 0));

            details.setPreferredSize(
                    new Dimension(POCKET_WIDTH, FOOTER_HEIGHT)
            );

            details.setMinimumSize(
                    new Dimension(POCKET_WIDTH, FOOTER_HEIGHT)
            );

            details.setMaximumSize(
                    new Dimension(POCKET_WIDTH, FOOTER_HEIGHT)
            );

            details.setBackground(Color.WHITE);

            if (index < cards.size()) {

                // BORDER ONLY FOR OCCUPIED POCKETS
                cardContainer.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 210, 210), 1
                        )
                );

                InventoryItem item = cards.get(index);
                Card card = item.getCard();

                // Load card at the correct aspect ratio
                ImageIcon image = CardImage.load(
                        card.getImagePath(),
                        POCKET_WIDTH,
                        CARD_DISPLAY_HEIGHT
                );

                JButton imageButton = new JButton();

                if (image != null) {
                    imageButton.setIcon(image);
                } else {
                    imageButton.setText("NO IMAGE");
                }

                imageButton.setPreferredSize(
                        new Dimension(POCKET_WIDTH, CARD_DISPLAY_HEIGHT)
                );

                imageButton.setBorder(null);
                imageButton.setContentAreaFilled(false);
                imageButton.setFocusPainted(false);
                imageButton.setMargin(new Insets(0, 0, 0, 0));
                imageButton.setOpaque(false);

                imageButton.addActionListener(e ->
                        onOpenCard.accept(item)
                );

                imagePanel.add(imageButton);

                // Card name
                JLabel name = new JLabel(
                        card.getName(),
                        SwingConstants.CENTER
                );

                name.setFont(
                        new Font("Arial", Font.BOLD, 10)
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
                        new Font("Arial", Font.PLAIN, 9)
                );

                priceLabel.setForeground(
                        new Color(90, 90, 90)
                );

                details.add(name);
                details.add(priceLabel);

                cardContainer.setToolTipText(card.getName());

            } else {

                // EMPTY POCKET — same image area as occupied pockets
                JLabel emptyLabel = new JLabel(
                        "EMPTY POCKET",
                        SwingConstants.CENTER
                );

                emptyLabel.setFont(
                        new Font("Arial", Font.PLAIN, 10)
                );

                emptyLabel.setForeground(
                        new Color(140, 140, 140)
                );

                imagePanel.add(emptyLabel);

                // Blank footer still occupies the same 30px
                details.add(new JLabel(""));
                details.add(new JLabel(""));
            }

            cardContainer.add(imagePanel, BorderLayout.CENTER);
            cardContainer.add(details, BorderLayout.SOUTH);


            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = i % 5;
            gbc.gridy = i / 5;

            gbc.insets = new Insets(3, 4, 3, 4);
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.NONE;

            cardsPanel.add(cardContainer, gbc);


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

// Match BOTH bottom panels to the full binder grid width
        int gridWidth = cardsPanel.getPreferredSize().width;

        Dimension navSize = navigationPanel.getPreferredSize();
        navigationPanel.setPreferredSize(
                new Dimension(gridWidth, navSize.height)
        );
        navigationPanel.setMaximumSize(
                new Dimension(gridWidth, navSize.height)
        );

        Dimension toolbarSize = pagesToolbar.getPreferredSize();
        pagesToolbar.setPreferredSize(
                new Dimension(gridWidth, toolbarSize.height)
        );
        pagesToolbar.setMaximumSize(
                new Dimension(gridWidth, toolbarSize.height)
        );

        navigationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pagesToolbar.setAlignmentX(Component.CENTER_ALIGNMENT);

        pagesMainPanel.revalidate();
        pagesMainPanel.repaint();
    }

    public JPanel getMainPanel() {
        return pagesMainPanel;
    }

    private void requireForm() {

        navigationPanel.setBorder(null);
        pagesToolbar.setBorder(null);

        int binderWidth = cardsPanel.getPreferredSize().width;

        navigationPanel.setPreferredSize(
                new Dimension(binderWidth, navigationPanel.getPreferredSize().height)
        );

        navigationPanel.setMaximumSize(
                new Dimension(binderWidth, navigationPanel.getPreferredSize().height)
        );

        pagesToolbar.setPreferredSize(
                new Dimension(binderWidth, pagesToolbar.getPreferredSize().height)
        );

        pagesToolbar.setMaximumSize(
                new Dimension(binderWidth, pagesToolbar.getPreferredSize().height)
        );

        navigationPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pagesToolbar.setAlignmentX(Component.CENTER_ALIGNMENT);

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
