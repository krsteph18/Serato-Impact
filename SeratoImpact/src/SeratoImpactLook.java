import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

/** Existing SeratoImpactLook.form, now showing any number of binders. */
public final class SeratoImpactLook {
    private JPanel mainPanel;
    private JPanel homePanel;
    private JLabel lblMainTitle;
    private JTextField txtSearch;
    private JLabel lblMyBinders;
    private JPanel binderGridPanel;
    private JPanel homeToolbar;
    private JButton btnCreateBinder, btnNewSeller, btnSwitchSeller, btnToggleMode;

    private final Marketplace marketplace;
    private final Consumer<Binder> onOpen;
    private User seller;
    private boolean sellerMode = true;

    public SeratoImpactLook(Marketplace marketplace, Consumer<Binder> onOpen,
                            Runnable newBinder, Runnable newSeller, Runnable switchSeller, Runnable toggleMode) {
        this.marketplace = marketplace;
        this.onOpen = onOpen;
        if (mainPanel == null) buildFallbackUI();
        reshapeHome();
        btnCreateBinder.addActionListener(e -> newBinder.run());
        btnNewSeller.addActionListener(e -> newSeller.run());
        btnSwitchSeller.addActionListener(e -> switchSeller.run());
        btnToggleMode.addActionListener(e -> toggleMode.run());
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refresh();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refresh();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refresh();
            }
        });
    }

    private void buildFallbackUI() {
        mainPanel = new JPanel(new BorderLayout());
        homePanel = new JPanel();
        lblMainTitle = new JLabel("SERATO IMPACT - DIGITAL BINDERS", SwingConstants.CENTER);
        txtSearch = new JTextField();
        lblMyBinders = new JLabel("MY BINDERS");
        binderGridPanel = new JPanel();
        homeToolbar = new JPanel();
        btnCreateBinder = new JButton("+ NEW BINDER");
        btnNewSeller = new JButton("+ SELLER");
        btnSwitchSeller = new JButton("SWITCH SELLER");
        btnToggleMode = new JButton("BUYER VIEW");
        mainPanel.add(homePanel, BorderLayout.CENTER);
    }

    private void reshapeHome() {
        homePanel.removeAll();
        homePanel.setLayout(new BorderLayout(12, 12));
        homePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        homePanel.setBackground(new Color(246, 243, 237));
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        lblMainTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblMainTitle.setForeground(new Color(44, 65, 54));
        lblMainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtSearch.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtSearch.setPreferredSize(new Dimension(600, 38));
        lblMyBinders.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblMyBinders.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(lblMainTitle);
        header.add(Box.createVerticalStrut(14));
        JLabel searchLabel = new JLabel("Search binders or cards");
        searchLabel.setForeground(new Color(66, 74, 68));
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(searchLabel);
        header.add(Box.createVerticalStrut(5));
        header.add(txtSearch);
        header.add(Box.createVerticalStrut(10));
        header.add(lblMyBinders);
        header.add(Box.createVerticalStrut(8));
        homeToolbar.removeAll();
        homeToolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        homeToolbar.setOpaque(false);
        homeToolbar.add(btnCreateBinder);
        homeToolbar.add(btnNewSeller);
        homeToolbar.add(btnSwitchSeller);
        homeToolbar.add(btnToggleMode);
        header.add(homeToolbar);
        homePanel.add(header, BorderLayout.NORTH);
        binderGridPanel.removeAll();
        binderGridPanel.setLayout(new GridLayout(0, 2, 12, 12));
        binderGridPanel.setBackground(new Color(246, 243, 237));
        JScrollPane scrollPane = new JScrollPane(binderGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        homePanel.add(scrollPane, BorderLayout.CENTER);
    }

    public void show(User seller, boolean sellerMode) {
        this.seller = seller;
        this.sellerMode = sellerMode;
        refresh();
    }

    public void refresh() {
        if (binderGridPanel == null || seller == null) return;
        binderGridPanel.removeAll();
        String q = txtSearch.getText().trim().toLowerCase();
        List<Binder> binders = sellerMode ? marketplace.bindersFor(seller) : marketplace.publicBinders();
        int shown = 0;
        for (Binder binder : binders) {
            boolean match = Marketplace.contains(binder.getName(), q) || Marketplace.contains(binder.getDescription(), q);
            if (!match) for (InventoryItem item : binder.getVisibleItems(sellerMode)) {
                Card card = item.getCard();
                if (Marketplace.contains(card.getName(), q) || Marketplace.contains(card.getCategory(), q)
                        || Marketplace.contains(card.getSetName(), q) || Marketplace.contains(card.getRarity(), q)) {
                    match = true;
                    break;
                }
            }
            if (!match) continue;
            shown++;
            User owner = marketplace.getOwner(binder);
            int count = binder.getVisibleItems(sellerMode).size();
            String caption = "<html><center><font color='#FFFFFF'><span style='font-size:17px'>" + CardImage.html(binder.getName()) + "</span>"
                    + "<br><br>" + count + " " + (sellerMode ? "card(s)" : "available card(s)")
                    + "<br>" + CardImage.html(owner == null ? "Unknown seller" : owner.getDisplayName())
                    + "<br><br>OPEN BINDER &gt;</font></center></html>";
            Color coverColor = shown % 4 == 1 ? new Color(43, 80, 64) : shown % 4 == 2 ? new Color(114, 80, 47) : shown % 4 == 3 ? new Color(54, 81, 111) : new Color(95, 65, 89);
            JButton b = new BinderTileButton(caption, coverColor);
            b.setPreferredSize(new Dimension(285, 190));
            b.setToolTipText(binder.getDescription());
            b.addActionListener(e -> onOpen.accept(binder));
            binderGridPanel.add(b);
        }
        if (shown == 0) {
            JLabel empty = new JLabel(sellerMode ? "No matching binders. Choose + NEW BINDER to start." : "No public binders match your search.");
            empty.setHorizontalAlignment(SwingConstants.CENTER);
            binderGridPanel.add(empty);
        }
        lblMyBinders.setText((sellerMode ? "MY BINDERS - " + seller.getDisplayName() : "PUBLIC BINDERS - BUYER VIEW") + " (" + shown + ")");
        txtSearch.setToolTipText("Search binders or cards by name, category, rarity, or set");
        btnToggleMode.setText(sellerMode ? "BUYER VIEW" : "SELLER VIEW");
        btnCreateBinder.setVisible(sellerMode);
        btnNewSeller.setVisible(sellerMode);
        btnSwitchSeller.setVisible(sellerMode);
        binderGridPanel.revalidate();
        binderGridPanel.repaint();
        homePanel.revalidate();
        homePanel.repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Windows Look and Feel can ignore JButton.setBackground() and paint a
     * white native button over the intended binder cover. Paint the cover
     * ourselves so its white title stays readable on every Look and Feel.
     * The tile remains a real JButton for keyboard and accessibility support.
     */
    private static final class BinderTileButton extends JButton {
        private final Color coverColor;

        BinderTileButton(String caption, Color coverColor) {
            super(caption);
            this.coverColor = coverColor;
            // Do not let WindowsButtonUI re-paint the tile with native white chrome.
            setUI(new BasicButtonUI());
            setForeground(Color.WHITE);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setMargin(new Insets(12, 12, 12, 12));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            try {
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = coverColor;
                if (getModel().isPressed()) color = color.darker();
                else if (getModel().isRollover()) color = color.brighter();
                g.setColor(color);
                g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 14, 14);
                if (isFocusOwner()) {
                    g.setColor(new Color(248, 208, 127));
                    g.setStroke(new BasicStroke(2f));
                    g.drawRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 12, 12);
                }
            } finally {
                g.dispose();
            }
            super.paintComponent(graphics);
        }
    }
}