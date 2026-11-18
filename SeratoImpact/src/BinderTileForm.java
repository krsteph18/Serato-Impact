import javax.swing.*;

/** Original character-green cover template; presentation lives in BinderTileForm.form. */
public final class BinderTileForm {
    private JPanel tilePanel;
    private JLabel lblBinderName;
    private JLabel lblCardCount;
    private JLabel lblSellerName;
    private JLabel lblOpen;

    public BinderTileForm(Binder binder, User owner, int cardCount, boolean sellerMode, Runnable open) {
        BinderTileBehavior.bind(tilePanel, lblBinderName, lblCardCount, lblSellerName,
                lblOpen, binder, owner, cardCount, sellerMode, open);
    }

    public JPanel getPanel() { return tilePanel; }
}
