import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public final class BinderPagesForm {
    private JPanel pagesMainPanel;
    private JLabel lblBinderName;
    private JPanel cardsPanel;
    private JButton btnCard1,btnCard2,btnCard3,btnCard4,btnCard5,btnCard6,btnCard7,btnCard8,btnCard9;
    private JPanel navigationPanel;
    private JButton btnBack,btnNextPage;
    private JLabel lblPageNumber;
    private JPanel pagesToolbar;
    private JButton btnAddCard,btnBackToBinders;
    private JButton[] cardButtons;
    private Binder binder;
    private boolean sellerMode;
    private int currentPage;
    private final Consumer<InventoryItem> onOpenCard;
    private final Runnable onBackToCover;

    public BinderPagesForm(Runnable onBackToCover,Runnable onBackToBinders,Runnable onAdd,Consumer<InventoryItem> onOpenCard) {
        this.onBackToCover=onBackToCover;
        this.onOpenCard=onOpenCard;
        requireForm();
        cardButtons=new JButton[]{btnCard1,btnCard2,btnCard3,btnCard4,btnCard5,btnCard6,btnCard7,btnCard8,btnCard9};
        for (JButton b : cardButtons) {
            b.addActionListener(e->{
                if (binder==null) return;
                int i=currentPage*9;
                for (int j=0;j<cardButtons.length;j++) if (cardButtons[j]==b) i+=j;
                List<InventoryItem> cards=binder.getVisibleItems(sellerMode);
                if (i<cards.size()) onOpenCard.accept(cards.get(i));
            });
        }
        btnNextPage.addActionListener(e->{if (currentPage<getTotalPages()-1){currentPage++;displayPage();}});
        btnBack.addActionListener(e->{if(currentPage>0){currentPage--;displayPage();}else onBackToCover.run();});
        btnBackToBinders.addActionListener(e->onBackToBinders.run());
        btnAddCard.addActionListener(e->onAdd.run());
    }

    public void setBinder(Binder binder,boolean sellerMode){
        this.binder=binder;this.sellerMode=sellerMode;currentPage=0;
        btnAddCard.setVisible(sellerMode);
        displayPage();
    }
    public void refresh(){displayPage();}
    private int getTotalPages(){return binder==null?1:binder.getTotalPages(sellerMode);}
    private void displayPage(){
        if(binder==null)return;
        List<InventoryItem> cards=binder.getVisibleItems(sellerMode);
        lblBinderName.setText(binder.getName()+" - "+(sellerMode?"MY COLLECTION":"FOR SALE"));
        if (currentPage>=getTotalPages())currentPage=getTotalPages()-1;
        for(int i=0;i<9;i++){
            JButton b=cardButtons[i];int index=currentPage*9+i;
            b.setIcon(null);
            if(index<cards.size()){
                InventoryItem item=cards.get(index);Card card=item.getCard();
                ImageIcon image=CardImage.load(card.getImagePath(),135,148);
                b.setIcon(image);
                String price=item.isAvailableForSale()?"PHP "+item.getListing().getPrice().toPlainString():"NOT FOR SALE";
                b.setText("<html><center>"+CardImage.html(card.getName())+"<br>"+price+"<br>Qty: "+item.getQuantity()+"</center></html>");
                b.setToolTipText(card.getCategory()+" - "+card.getSetName());
                b.setEnabled(true);
            }else{
                b.setText("EMPTY POCKET");b.setToolTipText(null);b.setEnabled(false);
            }
        }
        lblPageNumber.setText("Page "+(currentPage+1)+" of "+getTotalPages());
        btnNextPage.setEnabled(currentPage<getTotalPages()-1);
        btnBack.setText(currentPage==0?"BACK TO COVER":"PREVIOUS PAGE");
        pagesMainPanel.revalidate();pagesMainPanel.repaint();
    }
    public JPanel getMainPanel(){return pagesMainPanel;}

    private void requireForm() {
        if (pagesMainPanel == null || lblBinderName == null || cardsPanel == null || btnCard1 == null || btnCard2 == null || btnCard3 == null || btnCard4 == null || btnCard5 == null || btnCard6 == null || btnCard7 == null || btnCard8 == null || btnCard9 == null || navigationPanel == null || btnBack == null || btnNextPage == null || lblPageNumber == null || pagesToolbar == null || btnAddCard == null || btnBackToBinders == null) {
            throw new IllegalStateException("BinderPagesForm.form was not initialized. Build with IntelliJ Swing UI Designer (binary class files).");
        }
    }
}
