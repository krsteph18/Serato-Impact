import javax.swing.*;
import java.awt.*;

public final class BinderCoverForm {
    private JPanel coverMainPanel;
    private JPanel binderCoverPanel;
    private JLabel lblCoverImage, lblCoverTitle, lblCollectionName;
    private JButton btnOpenBinder, btnBackToCollection;
    private JPanel coverToolbar;
    private JButton btnAddCard, btnEditBinder, btnDeleteBinder;
    private Binder binder;

    public BinderCoverForm(Runnable onOpen, Runnable onBack, Runnable onAdd, Runnable onEdit, Runnable onDelete) {
        requireForm();
        btnOpenBinder.addActionListener(e->onOpen.run());
        btnBackToCollection.addActionListener(e->onBack.run());
        btnAddCard.addActionListener(e->onAdd.run());
        btnEditBinder.addActionListener(e->onEdit.run());
        btnDeleteBinder.addActionListener(e->onDelete.run());
    }

    public void setBinder(Binder binder,User owner,boolean sellerMode) {
        this.binder=binder;
        lblCoverTitle.setText("<html><center>"+CardImage.html(binder.getName())+"</center></html>");
        lblCollectionName.setText("<html><center>"+CardImage.html(owner==null?"Unknown seller":owner.getDisplayName())
                +"<br>"+CardImage.html(binder.getDescription())
                +"<br>"+binder.getVisibleItems(sellerMode).size()+" cards</center></html>");
        lblCoverImage.setText("DIGITAL BINDER");
        btnAddCard.setVisible(sellerMode);
        btnEditBinder.setVisible(sellerMode);
        btnDeleteBinder.setVisible(sellerMode);
        coverToolbar.setVisible(sellerMode);
        coverMainPanel.revalidate();coverMainPanel.repaint();
    }
    public JPanel getMainPanel(){return coverMainPanel;}

    private void requireForm() {
        if (coverMainPanel == null || binderCoverPanel == null || lblCoverImage == null || lblCoverTitle == null || lblCollectionName == null || btnOpenBinder == null || btnBackToCollection == null || coverToolbar == null || btnAddCard == null || btnEditBinder == null || btnDeleteBinder == null) {
            throw new IllegalStateException("BinderCoverForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
    }
}
