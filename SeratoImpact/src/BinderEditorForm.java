import javax.swing.*;
import java.awt.*;

/** IntelliJ GUI Designer form for editing a binder. */
public final class BinderEditorForm {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtDescription;
    private JCheckBox chkPublic;
    public BinderEditorForm(Binder existing) {
        requireForm();
        if (existing != null) {
            txtName.setText(existing.getName());
            txtDescription.setText(existing.getDescription());
            chkPublic.setSelected(existing.isPublic());
        } else chkPublic.setSelected(true);
    }

    public static BinderEditorForm show(Component owner, Binder existing) {
        BinderEditorForm form = new BinderEditorForm(existing);
        for (;;) {
            int choice = JOptionPane.showConfirmDialog(owner, form.mainPanel,
                    existing == null ? "Create Binder" : "Edit Binder", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice != JOptionPane.OK_OPTION) return null;
            if (!form.txtName.getText().trim().isEmpty()) return form;
            JOptionPane.showMessageDialog(owner, "Please enter a binder name.");
        }
    }
    public String getName() { return txtName.getText().trim(); }
    public String getDescription() { return txtDescription.getText().trim(); }
    public boolean isPublic() { return chkPublic.isSelected(); }

    private void requireForm() {
        if (mainPanel == null || txtName == null || txtDescription == null || chkPublic == null) {
            throw new IllegalStateException("BinderEditorForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
    }
}
