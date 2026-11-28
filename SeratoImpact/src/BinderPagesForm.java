
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.function.Consumer;

public final class BinderPagesForm {

    // ==================== COLOR PALETTE ====================

    private static final Color BASE = new Color(0x200D1B);
    private static final Color PLUM = new Color(0x44142C);
    private static final Color WINE = new Color(0x642444);
    private static final Color MIST = new Color(0xE4E4EC);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_WINE = new Color(0xD5ABC4);
    private static final Color BORDER = new Color(0xA46B8A);
    private static final Color MUTED = new Color(0xBA91A9);

    // ==================== CARD DIMENSIONS ====================

    private static final int PAGE_SIZE = 15;

    private static final int POCKET_WIDTH = 92;
    private static final int CARD_DISPLAY_WIDTH = POCKET_WIDTH - 6;
    private static final int CARD_DISPLAY_HEIGHT = 148;
    private static final int FOOTER_HEIGHT = 26;

    private static final int POCKET_HEIGHT =
            CARD_DISPLAY_HEIGHT + FOOTER_HEIGHT + 8;

    // ==================== SWING FORM COMPONENTS ====================

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

    // Additional components

    private JLabel lblSection;
    private JLabel lblSlotCount;

    private Binder binder;
    private boolean sellerMode;
    private int currentPage;

    private final Consumer<InventoryItem> onOpenCard;
    private final Runnable onBackToCover;

    // ==================== CONSTRUCTOR ====================

    public BinderPagesForm(
            Runnable onBackToCover,
            Runnable onBackToBinders,
            Runnable onAdd,
            Consumer<InventoryItem> onOpenCard
    ) {

        this.onBackToCover = onBackToCover;
        this.onOpenCard = onOpenCard;

        requireForm();
        buildLayout();

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
                this.onBackToCover.run();
            }
        });

        btnBackToBinders.addActionListener(e ->
                onBackToBinders.run()
        );

        btnAddCard.addActionListener(e ->
                onAdd.run()
        );
    }

    // ==================== FORM VALIDATION ====================

    private void requireForm() {

        if (pagesMainPanel == null ||
                lblBinderName == null ||
                cardsPanel == null ||
                navigationPanel == null ||
                btnBack == null ||
                btnNextPage == null ||
                lblPageNumber == null ||
                pagesToolbar == null ||
                btnAddCard == null ||
                btnBackToBinders == null) {

            throw new IllegalStateException(
                    "BinderPagesForm.form was not initialized. " +
                            "Check the Swing UI Designer plugin and form bindings."
            );
        }
    }

    // ==================== MAIN LAYOUT ====================

    private void buildLayout() {

        pagesMainPanel.removeAll();
        pagesMainPanel.setLayout(new BorderLayout(0, 8));
        pagesMainPanel.setBackground(BASE);
        pagesMainPanel.setOpaque(true);

        pagesMainPanel.setBorder(
                BorderFactory.createEmptyBorder(12, 24, 10, 24)
        );

        pagesMainPanel.setPreferredSize(
                new Dimension(900, 760)
        );

        // ==================== COMPACT HEADER ====================

        JPanel header = transparentPanel(
                new BorderLayout(16, 0)
        );

        JPanel heading = transparentPanel(null);

        heading.setLayout(
                new BoxLayout(heading, BoxLayout.Y_AXIS)
        );

        lblSection = label(
                "SERATO IMPACT / MY COLLECTION",
                LIGHT_WINE,
                Font.BOLD,
                12
        );

        lblBinderName.setText("Character Cards");
        lblBinderName.setIcon(null);
        lblBinderName.setPreferredSize(null);
        lblBinderName.setMinimumSize(null);
        lblBinderName.setMaximumSize(null);

        lblBinderName.setFont(
                new Font("SansSerif", Font.BOLD, 22)
        );

        lblBinderName.setForeground(WHITE);
        lblBinderName.setHorizontalAlignment(SwingConstants.LEFT);
        lblBinderName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = label(
                "Manage and explore your card collection.",
                MIST,
                Font.PLAIN,
                13
        );

        lblSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        heading.add(lblSection);
        heading.add(Box.createVerticalStrut(2));
        heading.add(lblBinderName);
        heading.add(Box.createVerticalStrut(2));
        heading.add(subtitle);

        // ==================== SLOT COUNTER ====================

        RoundedPanel stats = new RoundedPanel(
                WINE, null, false, 18
        );

        stats.setLayout(new GridLayout(2, 1, 0, 2));

        stats.setBorder(
                BorderFactory.createEmptyBorder(5, 14, 5, 14)
        );

        lblSlotCount = label(
                "0 / 15",
                WHITE,
                Font.BOLD,
                18
        );

        lblSlotCount.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel slots = label(
                "Page slots filled",
                MIST,
                Font.PLAIN,
                12
        );

        slots.setHorizontalAlignment(SwingConstants.CENTER);

        stats.add(lblSlotCount);
        stats.add(slots);

        header.add(heading, BorderLayout.CENTER);
        header.add(stats, BorderLayout.EAST);

        JPanel headerArea = transparentPanel(
                new BorderLayout(0, 7)
        );

        headerArea.add(header, BorderLayout.CENTER);
        headerArea.add(divider(), BorderLayout.SOUTH);

        pagesMainPanel.add(headerArea, BorderLayout.NORTH);

        // ==================== BINDER PANEL ====================

        cardsPanel.removeAll();
        cardsPanel.setLayout(new GridBagLayout());
        cardsPanel.setOpaque(false);

        cardsPanel.setBorder(
                BorderFactory.createEmptyBorder(3, 8, 3, 8)
        );

        cardsPanel.setPreferredSize(null);
        cardsPanel.setMinimumSize(null);
        cardsPanel.setMaximumSize(null);

        RoundedPanel binderSurface = new RoundedPanel(
                PLUM, null, false, 22
        );

        binderSurface.setLayout(new GridBagLayout());

        GridBagConstraints center = new GridBagConstraints();
        center.anchor = GridBagConstraints.CENTER;

        binderSurface.add(cardsPanel, center);

        // Keep the binder surface close to the card grid

        JPanel binderWrapper = transparentPanel(
                new GridBagLayout()
        );

        GridBagConstraints wrapperConstraints =
                new GridBagConstraints();

        wrapperConstraints.weightx = 1.0;
        wrapperConstraints.weighty = 1.0;
        wrapperConstraints.fill = GridBagConstraints.BOTH;
        wrapperConstraints.anchor = GridBagConstraints.CENTER;

        wrapperConstraints.insets = new Insets(15, 0, 15, 0);

        binderWrapper.add(binderSurface, wrapperConstraints);

        // Scrolling is only used when the window is too small

        JScrollPane scroll = new JScrollPane(
                binderWrapper,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        pagesMainPanel.add(scroll, BorderLayout.CENTER);

        // ==================== PAGE NAVIGATION ====================

        navigationPanel.removeAll();

        navigationPanel.setLayout(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        navigationPanel.setOpaque(false);
        navigationPanel.setPreferredSize(null);
        navigationPanel.setMinimumSize(null);
        navigationPanel.setMaximumSize(null);

        navigationPanel.setBorder(
                BorderFactory.createEmptyBorder()
        );

        lblPageNumber.setForeground(MIST);
        lblPageNumber.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        lblPageNumber.setText("Page 1 of 1");

        btnBack.setText("\u2039 Back to cover");
        btnNextPage.setText("Next page \u203a");

        styleButton(btnBack, false);
        styleButton(btnNextPage, false);

        btnNextPage.setEnabled(false);

        navigationPanel.add(btnBack);
        navigationPanel.add(lblPageNumber);
        navigationPanel.add(btnNextPage);

        // ==================== COMBINED BOTTOM TOOLBAR ====================

        pagesToolbar.removeAll();

        pagesToolbar.setLayout(new BorderLayout(12, 0));
        pagesToolbar.setOpaque(false);
        pagesToolbar.setPreferredSize(null);
        pagesToolbar.setMinimumSize(null);
        pagesToolbar.setMaximumSize(null);

        pagesToolbar.setBorder(
                BorderFactory.createEmptyBorder(4, 0, 0, 0)
        );

        btnBackToBinders.setText("My binders");
        btnAddCard.setText("+ Add card");

        styleButton(btnBackToBinders, false);
        styleButton(btnAddCard, true);

        btnAddCard.setVisible(false);

        // Left: My binders

        pagesToolbar.add(
                btnBackToBinders,
                BorderLayout.WEST
        );

        // Center: Page navigation

        pagesToolbar.add(
                navigationPanel,
                BorderLayout.CENTER
        );

        // Right: Add card

        pagesToolbar.add(
                btnAddCard,
                BorderLayout.EAST
        );

        JPanel bottom = transparentPanel(
                new BorderLayout(0, 6)
        );

        bottom.add(divider(), BorderLayout.NORTH);
        bottom.add(pagesToolbar, BorderLayout.CENTER);

        pagesMainPanel.add(bottom, BorderLayout.SOUTH);

        // Display initial 15 empty card pockets

        for (int i = 0; i < PAGE_SIZE; i++) {
            addPocket(createEmptyPocket(i + 1), i);
        }
    }

    // ==================== BINDER MANAGEMENT ====================

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

        if (binder == null) {
            return 1;
        }

        int count = binder.getVisibleItems(sellerMode).size();

        return Math.max(
                1,
                (count + PAGE_SIZE - 1) / PAGE_SIZE
        );
    }

    // ==================== DISPLAY PAGE ====================

    private void displayPage() {

        if (binder == null) {
            return;
        }

        List<InventoryItem> cards =
                binder.getVisibleItems(sellerMode);

        int totalPages = Math.max(
                1,
                (cards.size() + PAGE_SIZE - 1) / PAGE_SIZE
        );

        currentPage = Math.max(
                0,
                Math.min(currentPage, totalPages - 1)
        );

        lblBinderName.setText(binder.getName());

        lblSection.setText(
                "SERATO IMPACT / " +
                        (sellerMode ? "MY COLLECTION" : "FOR SALE")
        );

        int start = currentPage * PAGE_SIZE;

        int filled = Math.min(
                PAGE_SIZE,
                Math.max(0, cards.size() - start)
        );

        lblSlotCount.setText(filled + " / " + PAGE_SIZE);

        cardsPanel.removeAll();

        for (int i = 0; i < PAGE_SIZE; i++) {

            int index = start + i;

            JPanel pocket;

            if (index < cards.size()) {
                pocket = createOccupiedPocket(cards.get(index));
            } else {
                pocket = createEmptyPocket(i + 1);
            }

            addPocket(pocket, i);
        }

        lblPageNumber.setText(
                "Page " + (currentPage + 1) +
                        " of " + totalPages
        );

        btnNextPage.setEnabled(
                currentPage < totalPages - 1
        );

        btnBack.setText(
                currentPage == 0
                        ? "\u2039 Back to cover"
                        : "\u2039 Previous page"
        );

        cardsPanel.revalidate();
        cardsPanel.repaint();

        pagesMainPanel.revalidate();
        pagesMainPanel.repaint();
    }

    // ==================== GRID POSITIONING ====================

    private void addPocket(JPanel pocket, int index) {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = index % 5;
        gbc.gridy = index / 5;

        // Reduced vertical spacing between pockets

        gbc.insets = new Insets(3, 4, 3, 4);

        cardsPanel.add(pocket, gbc);
    }

    // ==================== EMPTY CARD POCKET ====================

    private JPanel createEmptyPocket(int number) {

        RoundedPanel pocket = new RoundedPanel(
                WINE,
                BORDER,
                true,
                14
        );

        pocket.setPreferredSize(
                new Dimension(POCKET_WIDTH, POCKET_HEIGHT)
        );

        pocket.setLayout(new GridBagLayout());

        JPanel content = transparentPanel(null);

        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        JLabel icon = new JLabel(new StackIcon());

        JLabel empty = label(
                "Empty",
                MIST,
                Font.PLAIN,
                13
        );

        JLabel slot = label(
                String.format("%02d", number),
                LIGHT_WINE,
                Font.PLAIN,
                12
        );

        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        empty.setAlignmentX(Component.CENTER_ALIGNMENT);
        slot.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(icon);
        content.add(Box.createVerticalStrut(7));
        content.add(empty);
        content.add(Box.createVerticalStrut(3));
        content.add(slot);

        pocket.add(content);

        return pocket;
    }

    // ==================== OCCUPIED CARD POCKET ====================

    private JPanel createOccupiedPocket(InventoryItem item) {

        Card card = item.getCard();

        RoundedPanel pocket = new RoundedPanel(
                WINE,
                BORDER,
                false,
                14
        );

        pocket.setPreferredSize(
                new Dimension(POCKET_WIDTH, POCKET_HEIGHT)
        );

        pocket.setLayout(new BorderLayout(0, 2));

        // Minimal padding around the actual card

        pocket.setBorder(
                BorderFactory.createEmptyBorder(3, 3, 3, 3)
        );

        ImageIcon image = CardImage.load(
                card.getImagePath(),
                CARD_DISPLAY_WIDTH,
                CARD_DISPLAY_HEIGHT
        );

        JButton imageButton = new JButton();

        if (image != null) {
            imageButton.setIcon(image);
        } else {
            imageButton.setText("NO IMAGE");
        }

        imageButton.setForeground(MIST);
        imageButton.setFont(
                new Font("SansSerif", Font.PLAIN, 10)
        );

        imageButton.setBorder(
                BorderFactory.createEmptyBorder()
        );

        imageButton.setMargin(new Insets(0, 0, 0, 0));
        imageButton.setContentAreaFilled(false);
        imageButton.setOpaque(false);
        imageButton.setFocusPainted(false);

        imageButton.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        imageButton.setToolTipText(card.getName());

        imageButton.addActionListener(e ->
                onOpenCard.accept(item)
        );

        pocket.add(imageButton, BorderLayout.CENTER);

        // ==================== CARD DETAILS ====================

        JPanel details = transparentPanel(
                new GridLayout(2, 1)
        );

        details.setPreferredSize(
                new Dimension(
                        POCKET_WIDTH - 10,
                        FOOTER_HEIGHT
                )
        );

        // Card name

        JLabel name = label(
                card.getName(),
                WHITE,
                Font.BOLD,
                10
        );

        name.setHorizontalAlignment(SwingConstants.CENTER);

        // Card price

        String price = "";

        if (item.isAvailableForSale() &&
                item.getListing() != null &&
                item.getListing().getPrice() != null) {

            price = "PHP " +
                    item.getListing()
                            .getPrice()
                            .toPlainString();
        }

        JLabel priceLabel = label(
                price,
                MIST,
                Font.PLAIN,
                10
        );

        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        details.add(name);
        details.add(priceLabel);

        pocket.add(details, BorderLayout.SOUTH);
        pocket.setToolTipText(card.getName());

        return pocket;
    }

    // ==================== PANEL HELPER ====================

    private static JPanel transparentPanel(LayoutManager layout) {

        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);

        return panel;
    }

    // ==================== LABEL HELPER ====================

    private static JLabel label(
            String text,
            Color color,
            int style,
            int size
    ) {

        JLabel label = new JLabel(text);

        label.setForeground(color);
        label.setFont(new Font("SansSerif", style, size));

        return label;
    }

    // ==================== DIVIDER ====================

    private static JPanel divider() {

        JPanel line = new JPanel();

        line.setBackground(BORDER);
        line.setPreferredSize(new Dimension(1, 1));

        return line;
    }

    // ==================== BUTTON STYLING ====================

    private static void styleButton(
            JButton button,
            boolean primary
    ) {

        button.setUI(new BasicButtonUI() {

            @Override
            public void paint(Graphics g, JComponent c) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                ButtonModel model = button.getModel();

                Color fill;

                if (!button.isEnabled()) {
                    fill = WINE.darker();

                } else if (primary) {
                    fill = model.isRollover() ? WHITE : MIST;

                } else if (model.isPressed()) {
                    fill = WINE;

                } else if (model.isRollover()) {
                    fill = PLUM;

                } else {
                    fill = BASE;
                }

                g2.setColor(fill);

                g2.fillRoundRect(
                        0,
                        0,
                        c.getWidth() - 1,
                        c.getHeight() - 1,
                        14,
                        14
                );

                if (!primary && button.isEnabled()) {

                    g2.setColor(BORDER);

                    g2.drawRoundRect(
                            0,
                            0,
                            c.getWidth() - 1,
                            c.getHeight() - 1,
                            14,
                            14
                    );
                }

                if (button.hasFocus() && button.isEnabled()) {

                    g2.setColor(primary ? PLUM : MIST);

                    g2.drawRoundRect(
                            3,
                            3,
                            c.getWidth() - 7,
                            c.getHeight() - 7,
                            10,
                            10
                    );
                }

                g2.dispose();

                super.paint(g, c);
            }

            @Override
            protected void paintText(
                    Graphics g,
                    AbstractButton b,
                    Rectangle r,
                    String text
            ) {

                FontMetrics fm = g.getFontMetrics();

                g.setColor(
                        b.isEnabled()
                                ? (primary ? PLUM : WHITE)
                                : MUTED
                );

                g.drawString(
                        text,
                        r.x,
                        r.y + fm.getAscent()
                );
            }
        });

        button.setFont(
                new Font(
                        "SansSerif",
                        primary ? Font.BOLD : Font.PLAIN,
                        13
                )
        );

        button.setForeground(primary ? PLUM : WHITE);

        // Reduced button height

        button.setBorder(
                BorderFactory.createEmptyBorder(7, 14, 7, 14)
        );

        button.setPreferredSize(null);
        button.setMinimumSize(null);
        button.setMaximumSize(null);

        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);

        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
    }

    // ==================== ROUNDED PANEL ====================

    private static final class RoundedPanel extends JPanel {

        private final Color fill;
        private final Color outline;
        private final boolean dashed;
        private final int arc;

        RoundedPanel(
                Color fill,
                Color outline,
                boolean dashed,
                int arc
        ) {

            this.fill = fill;
            this.outline = outline;
            this.dashed = dashed;
            this.arc = arc;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Shape shape = new RoundRectangle2D.Float(
                    0.5f,
                    0.5f,
                    getWidth() - 1f,
                    getHeight() - 1f,
                    arc,
                    arc
            );

            g2.setColor(fill);
            g2.fill(shape);

            if (outline != null) {

                g2.setColor(outline);

                if (dashed) {

                    g2.setStroke(
                            new BasicStroke(
                                    1f,
                                    BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND,
                                    10f,
                                    new float[]{4f, 4f},
                                    0f
                            )
                    );

                } else {
                    g2.setStroke(new BasicStroke(1f));
                }

                g2.draw(shape);
            }

            g2.dispose();
        }
    }

    // ==================== CARD STACK ICON ====================

    private static final class StackIcon implements Icon {

        @Override
        public int getIconWidth() {
            return 24;
        }

        @Override
        public int getIconHeight() {
            return 20;
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.translate(x, y);

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(MIST);

            g2.setStroke(
                    new BasicStroke(
                            1.6f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g2.drawPolygon(
                    new int[]{3, 7, 17, 21},
                    new int[]{8, 3, 3, 8},
                    4
            );

            g2.drawLine(4, 12, 20, 12);
            g2.drawLine(6, 16, 18, 16);

            g2.dispose();
        }
    }

    // ==================== GET MAIN PANEL ====================

    public JPanel getMainPanel() {
        return pagesMainPanel;
    }
}
