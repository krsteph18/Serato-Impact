import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class BinderTileBehavior {
    private BinderTileBehavior() { }

    public static void bind(JPanel panel, JLabel title, JLabel count, JLabel seller,
                            JLabel openLabel, Binder binder, User owner,
                            int cardCount, boolean sellerMode, Runnable open) {
        if (panel == null || title == null || count == null || seller == null || openLabel == null) {
            throw new IllegalStateException("Binder tile .form was not initialized. Rebuild with IntelliJ GUI Designer enabled.");
        }
        title.setText(binder.getName());
        count.setText(cardCount + (sellerMode ? " card(s)" : " available card(s)"));
        seller.setText(owner == null ? "Unknown seller" : owner.getDisplayName());
        panel.setToolTipText(binder.getDescription());
        panel.getAccessibleContext().setAccessibleName("Open binder " + binder.getName());
        MouseAdapter click = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) open.run();
            }
        };
        panel.addMouseListener(click);
        title.addMouseListener(click);
        count.addMouseListener(click);
        seller.addMouseListener(click);
        openLabel.addMouseListener(click);
        panel.setFocusable(true);
        panel.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "openBinder");
        panel.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "openBinder");
        panel.getActionMap().put("openBinder", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { open.run(); }
        });
    }
}
