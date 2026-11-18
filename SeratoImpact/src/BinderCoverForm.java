import javax.swing.*;
import java.awt.*;

/** Existing IntelliJ BinderCoverForm.form, with seller management controls. */
public final class BinderCoverForm {
    private JPanel coverMainPanel;
    private JPanel binderCoverPanel;
    private JLabel lblCoverImage, lblCoverTitle, lblCollectionName;
    private JButton btnOpenBinder, btnBackToCollection;
    private JPanel coverToolbar;
    private JButton btnAddCard, btnEditBinder, btnDeleteBinder;
    private Binder binder;

    public BinderCoverForm(Runnable onOpen, Runnable onBack, Runnable onAdd, Runnable onEdit, Runnable onDelete) {
        if (coverMainPanel==null) buildFallbackUI();
        btnOpenBinder.addActionListener(e->onOpen.run());
        btnBackToCollection.addActionListener(e->onBack.run());
        btnAddCard.addActionListener(e->onAdd.run());
        btnEditBinder.addActionListener(e->onEdit.run());
        btnDeleteBinder.addActionListener(e->onDelete.run());
    }
    private void buildFallbackUI() {
        coverMainPanel=new JPanel(new BorderLayout(12,14));
        binderCoverPanel=new JPanel(new GridLayout(3,1,6,10));
        lblCoverImage=new JLabel("COLLECTION",SwingConstants.CENTER);
        lblCoverTitle=new JLabel("BINDER",SwingConstants.CENTER);
        lblCollectionName=new JLabel("SELLER",SwingConstants.CENTER);
        binderCoverPanel.add(lblCoverImage);binderCoverPanel.add(lblCoverTitle);binderCoverPanel.add(lblCollectionName);
        JPanel center=new JPanel(new BorderLayout());center.add(binderCoverPanel,BorderLayout.CENTER);
        center.setBorder(BorderFactory.createEmptyBorder(30,260,20,260));
        coverMainPanel.add(center,BorderLayout.CENTER);
        JPanel bottom=new JPanel(new GridLayout(3,1,6,6));
        btnOpenBinder=new JButton("OPEN BINDER");btnBackToCollection=new JButton("BACK TO COLLECTION");
        coverToolbar=new JPanel(new FlowLayout(FlowLayout.CENTER,9,4));
        btnAddCard=new JButton("+ ADD CARD");btnEditBinder=new JButton("EDIT BINDER");btnDeleteBinder=new JButton("DELETE BINDER");
        coverToolbar.add(btnAddCard);coverToolbar.add(btnEditBinder);coverToolbar.add(btnDeleteBinder);
        bottom.add(btnOpenBinder);bottom.add(coverToolbar);bottom.add(btnBackToCollection);
        coverMainPanel.add(bottom,BorderLayout.SOUTH);
    }
    public void setBinder(Binder binder,User owner,boolean sellerMode) {
        this.binder=binder;
        lblCoverTitle.setText("<html><center>"+CardImage.html(binder.getName())+"</center></html>");
        lblCollectionName.setText("<html><center>"+CardImage.html(owner==null?"Unknown seller":owner.getDisplayName())
                +"<br>"+CardImage.html(binder.getDescription())
                +"<br>"+binder.getVisibleItems(sellerMode).size()+" cards</center></html>");
        lblCoverImage.setText("DIGITAL BINDER");
        lblCoverImage.setOpaque(true);
        lblCoverImage.setBackground(new Color(42,75,61));
        lblCoverImage.setForeground(Color.WHITE);
        lblCoverImage.setHorizontalAlignment(SwingConstants.CENTER);
        btnAddCard.setVisible(sellerMode);
        btnEditBinder.setVisible(sellerMode);
        btnDeleteBinder.setVisible(sellerMode);
        coverToolbar.setVisible(sellerMode);
        coverMainPanel.revalidate();coverMainPanel.repaint();
    }
    public JPanel getMainPanel(){return coverMainPanel;}
}
