import javax.swing.*;

public final class BinderTileEquipmentForm {
    private JPanel tilePanel;
    private JLabel lblBinderName;
    private JLabel lblCardCount;
    private JLabel lblSellerName;
    private JLabel lblOpen;

    public BinderTileEquipmentForm(Binder binder, User owner, int cardCount, boolean sellerMode, Runnable open) {
        BinderTileBehavior.bind(tilePanel, lblBinderName, lblCardCount, lblSellerName,
                lblOpen, binder, owner, cardCount, sellerMode, open);
    }

    public JPanel getPanel() { return tilePanel; }
}
