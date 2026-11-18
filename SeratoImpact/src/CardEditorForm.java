import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

/** Card details / sales listing editor, backed by IntelliJ CardEditorForm.form. */
public final class CardEditorForm {
    private JPanel mainPanel;
    private JTextField txtName, txtCategory, txtSet, txtRarity, txtQuantity, txtPrice, txtImagePath;
    private JComboBox<String> cmbCondition;
    private JCheckBox chkForSale;
    private JButton btnBrowseImage;

    public CardEditorForm(InventoryItem existing) {
        requireForm();
        btnBrowseImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Choose card image (JPEG or PNG)");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png"));
            if (chooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION)
                txtImagePath.setText(chooser.getSelectedFile().getAbsolutePath());
        });
        if (existing != null) {
            Card c = existing.getCard();
            txtName.setText(c.getName());txtCategory.setText(c.getCategory());
            txtSet.setText(c.getSetName());txtRarity.setText(c.getRarity());
            txtImagePath.setText(c.getImagePath());
            cmbCondition.setSelectedItem(existing.getCondition());
            txtQuantity.setText(String.valueOf(existing.getQuantity()));
            txtPrice.setText(existing.getListing().getPrice().toPlainString());
            chkForSale.setSelected(existing.getListing().isForSale());
        } else {
            txtQuantity.setText("1");txtPrice.setText("0");chkForSale.setSelected(false);
        }
    }


    public static CardEditorForm show(Component parent, InventoryItem existing) {
        CardEditorForm form = new CardEditorForm(existing);
        for (;;) {
            int choice = JOptionPane.showConfirmDialog(parent, new JScrollPane(form.mainPanel),
                    existing == null ? "Add Card to Binder" : "Edit Card", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice != JOptionPane.OK_OPTION) return null;
            try { form.validateFields(); return form; }
            catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "Check card details", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private void validateFields() {
        if (txtName.getText().trim().isEmpty()) throw new IllegalArgumentException("Card name is required.");
        try {
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            BigDecimal price = new BigDecimal(txtPrice.getText().trim());
            if (qty < 0) throw new IllegalArgumentException("Quantity cannot be negative.");
            if (price.signum() < 0) throw new IllegalArgumentException("Price cannot be negative.");
            if (chkForSale.isSelected() && (qty < 1 || price.signum() <= 0))
                throw new IllegalArgumentException("For-sale cards need positive stock and price.");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Quantity must be an integer, and price must be a number.");
        }
        String path = txtImagePath.getText().trim();
        if (!path.isEmpty() && !java.nio.file.Files.isRegularFile(java.nio.file.Path.of(path)))
            throw new IllegalArgumentException("Image file does not exist. Select an existing JPG or PNG.");
    }
    public InventoryItem createItem() throws java.io.IOException {
        Card card = new Card(getName(),txtCategory.getText(),txtSet.getText(),txtRarity.getText(),CardImage.importImage(txtImagePath.getText()));
        return new InventoryItem(card,String.valueOf(cmbCondition.getSelectedItem()),getQuantity(),getPrice(),chkForSale.isSelected());
    }
    public void updateItem(InventoryItem item) throws java.io.IOException {
        String path = txtImagePath.getText().trim();
        String existingPath = item.getCard().getImagePath();
        if (!path.equals(existingPath)) path = CardImage.importImage(path);
        item.getCard().update(getName(),txtCategory.getText(),txtSet.getText(),txtRarity.getText(),path);
        item.update(String.valueOf(cmbCondition.getSelectedItem()),getQuantity(),getPrice(),chkForSale.isSelected());
    }
    private String getName() { return txtName.getText().trim(); }
    private int getQuantity() { return Integer.parseInt(txtQuantity.getText().trim()); }
    private BigDecimal getPrice() { return new BigDecimal(txtPrice.getText().trim()); }

    private void requireForm() {
        if (mainPanel == null || txtName == null || txtCategory == null || txtSet == null || txtRarity == null || txtQuantity == null || txtPrice == null || txtImagePath == null || cmbCondition == null || chkForSale == null || btnBrowseImage == null) {
            throw new IllegalStateException("CardEditorForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
    }
}
