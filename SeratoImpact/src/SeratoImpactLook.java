import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;
import java.util.function.Consumer;

/** Controller for SeratoImpactLook.form: layout and styling live in the form. */
public final class SeratoImpactLook {
    private JPanel mainPanel;
    private JPanel homePanel;
    private JLabel lblMainTitle;
    private JTextField txtSearch;
    private JLabel lblMyBinders;
    private JPanel binderGridPanel;
    private JScrollPane bindersScrollPane;
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
        if (mainPanel == null || binderGridPanel == null || txtSearch == null || bindersScrollPane == null) {
            throw new IllegalStateException("SeratoImpactLook.form was not initialized. In IntelliJ, enable Swing UI Designer and generate GUI into binary class files.");
        }
        btnCreateBinder.addActionListener(e -> newBinder.run());
        btnNewSeller.addActionListener(e -> newSeller.run());
        btnSwitchSeller.addActionListener(e -> switchSeller.run());
        btnToggleMode.addActionListener(e -> toggleMode.run());
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { refresh(); }
            @Override public void removeUpdate(DocumentEvent e) { refresh(); }
            @Override public void changedUpdate(DocumentEvent e) { refresh(); }
        });
    }

    public void show(User seller, boolean sellerMode) {
        this.seller = seller;
        this.sellerMode = sellerMode;
        refresh();
    }

    public void refresh() {
        if (seller == null) return;
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
            User owner = marketplace.getOwner(binder);
            int count = binder.getVisibleItems(sellerMode).size();
            // The original four colors are stored in four editable .form cover templates.
            // User-created binders cycle through those same original templates.
            int template = marketplace.getBinders().indexOf(binder) % 4;
            String name = binder.getName().toLowerCase(java.util.Locale.ROOT);
            if (name.equals("character cards")) template = 0;
            else if (name.equals("equipment cards")) template = 1;
            else if (name.equals("event cards")) template = 2;
            else if (name.equals("support cards")) template = 3;
            Runnable open = () -> onOpen.accept(binder);
            JPanel tilePanel;
            switch (template) {
                case 1 -> tilePanel = new BinderTileEquipmentForm(binder, owner, count, sellerMode, open).getPanel();
                case 2 -> tilePanel = new BinderTileEventForm(binder, owner, count, sellerMode, open).getPanel();
                case 3 -> tilePanel = new BinderTileSupportForm(binder, owner, count, sellerMode, open).getPanel();
                default -> tilePanel = new BinderTileForm(binder, owner, count, sellerMode, open).getPanel();
            }
            // Only dynamic tile placement is done in Java; styling stays in .form files.
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = shown % 2;
            gbc.gridy = shown / 2;
            gbc.weightx = 0.5;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTH;
            gbc.insets = new Insets(6, 6, 6, 6);
            binderGridPanel.add(tilePanel, gbc);
            shown++;
        }
        if (shown == 0) {
            // Dynamic empty state text; no GUI layout manipulation.
            lblMyBinders.setText(sellerMode ? "MY BINDERS — No matching binders. Use + NEW BINDER." : "PUBLIC BINDERS — No matching results.");
        } else {
            lblMyBinders.setText((sellerMode ? "MY BINDERS - " + seller.getDisplayName() : "PUBLIC BINDERS - BUYER VIEW") + " (" + shown + ")");
        }
        btnToggleMode.setText(sellerMode ? "BUYER VIEW" : "SELLER VIEW");
        btnCreateBinder.setVisible(sellerMode);
        btnNewSeller.setVisible(sellerMode);
        btnSwitchSeller.setVisible(sellerMode);
        binderGridPanel.revalidate();
        binderGridPanel.repaint();
    }

    public JPanel getMainPanel() { return mainPanel; }
}
