import javax.swing.*;

public final class CardDetailsForm {
    private JPanel mainPanel;
    private JPanel infoPanel;
    private JLabel lblCardImage;
    private JLabel lblName;
    private JLabel lblCategory;
    private JLabel lblSet;
    private JLabel lblRarity;
    private JLabel lblCondition;
    private JLabel lblStock;
    private JLabel lblPrice;
    private JLabel lblSeller;
    private JLabel lblContact;

    public CardDetailsForm(InventoryItem item, User owner) {
        if (mainPanel == null || lblCardImage == null || lblName == null || lblContact == null) {
            throw new IllegalStateException("CardDetailsForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
        Card card = item.getCard();
        ImageIcon image = CardImage.load(card.getImagePath(), 220, 300);
        lblCardImage.setIcon(image);
        lblCardImage.setText(image == null ? "NO IMAGE" : "");
        lblName.setText(card.getName());
        lblCategory.setText(card.getCategory());
        lblSet.setText(card.getSetName());
        lblRarity.setText(card.getRarity());
        lblCondition.setText(item.getCondition());
        lblStock.setText(Integer.toString(item.getQuantity()));
        lblPrice.setText(item.isAvailableForSale() ? "PHP " + item.getListing().getPrice().toPlainString() : "NOT FOR SALE");
        lblSeller.setText(owner == null ? "Unknown" : owner.getDisplayName());
        lblContact.setText(owner == null ? "Not provided" : owner.getContact());
    }

    public JPanel getPanel() { return mainPanel; }
}
