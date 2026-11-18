import javax.swing.*;

/** Support cover template; presentation lives in BinderTileSupportForm.form. */
public final class BinderTileSupportForm {
    private JPanel tilePanel;
    private JLabel lblBinderName;
    private JLabel lblCardCount;
    private JLabel lblSellerName;
    private JLabel lblOpen;

    public BinderTileSupportForm(Binder binder, User owner, int cardCount, boolean sellerMode, Runnable open) {
        BinderTileBehavior.bind(tilePanel, lblBinderName, lblCardCount, lblSellerName,
                lblOpen, binder, owner, cardCount, sellerMode, open);
    }

    public JPanel getPanel() { return tilePanel; }
}
