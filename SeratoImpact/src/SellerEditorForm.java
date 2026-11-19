import javax.swing.*;
import java.awt.*;

public final class SellerEditorForm {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtContact;
    public SellerEditorForm(User existing) {
        requireForm();
        if (existing != null) {
            txtName.setText(existing.getDisplayName());
            txtContact.setText(existing.getContact());
        }
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

    private void requireForm() {
        if (mainPanel == null || txtName == null || txtContact == null) {
            throw new IllegalStateException("SellerEditorForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
    }
}
