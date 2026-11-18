import javax.swing.*;
import java.awt.*;

/** IntelliJ GUI Designer form for a local seller profile. */
public final class SellerEditorForm {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtContact;
    public SellerEditorForm(User existing) {
        if (mainPanel == null) buildFallbackUI();
        if (existing != null) {
            txtName.setText(existing.getDisplayName());
            txtContact.setText(existing.getContact());
        }
    }
    private void buildFallbackUI() {
        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        txtName = new JTextField(24);txtContact = new JTextField(24);
        mainPanel.add(new JLabel("Seller display name")); mainPanel.add(txtName);
        mainPanel.add(new JLabel("Contact / message details")); mainPanel.add(txtContact);
    }
    public static SellerEditorForm show(Component parent, User existing) {
        SellerEditorForm form = new SellerEditorForm(existing);
        for (;;) {
            int result = JOptionPane.showConfirmDialog(parent, form.mainPanel,
                    existing == null ? "New Seller Profile" : "Edit Seller Profile", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) return null;
            if (!form.txtName.getText().trim().isEmpty()) return form;
            JOptionPane.showMessageDialog(parent, "Seller name is required.");
        }
    }
    public String getName() { return txtName.getText().trim(); }
    public String getContact() { return txtContact.getText().trim(); }
}
