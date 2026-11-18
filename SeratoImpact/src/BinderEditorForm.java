import javax.swing.*;
import java.awt.*;

/** IntelliJ GUI Designer form for editing a binder. */
public final class BinderEditorForm {
    private JPanel mainPanel;
    private JTextField txtName;
    private JTextField txtDescription;
    private JCheckBox chkPublic;
    public BinderEditorForm(Binder existing) {
        if (mainPanel == null) buildFallbackUI();
        if (existing != null) {
            txtName.setText(existing.getName());
            txtDescription.setText(existing.getDescription());
            chkPublic.setSelected(existing.isPublic());
        } else chkPublic.setSelected(true);
    }
    private void buildFallbackUI() {
        mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        txtName = new JTextField(24); txtDescription = new JTextField(24);
        chkPublic = new JCheckBox("Public: buyers can view", true);
        mainPanel.add(new JLabel("Binder name"));mainPanel.add(txtName);
        mainPanel.add(new JLabel("Description"));mainPanel.add(txtDescription);
        mainPanel.add(new JLabel("Visibility"));mainPanel.add(chkPublic);
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
}
