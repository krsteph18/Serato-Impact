import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public final class Main {
    private final MarketplaceRepository repository=new MarketplaceRepository();
    private Marketplace marketplace;
    private User currentSeller;
    private Binder currentBinder;
    private boolean sellerMode=true;
    private JFrame frame;
    private CardLayout navigation;
    private JPanel screens;
    private SeratoImpactLook home;
    private BinderCoverForm cover;
    private BinderPagesForm pages;

    public static void main(String[] args){
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch (Exception ignored) { }
        SwingUtilities.invokeLater(()->new Main().launch());
    }
    private void launch() {
        try {marketplace=repository.load();}
        catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(null,ex.getMessage()+"\nThe data file was not overwritten.",
                    "Cannot load saved marketplace",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(marketplace.getSellers().isEmpty()) marketplace.addSeller("Sample Seller","");
        currentSeller=marketplace.getSellers().get(0);
        navigation=new CardLayout();screens=new JPanel(navigation);
        pages=new BinderPagesForm(this::showCover,this::showHome,this::addCard,this::showCardDetails);
        cover=new BinderCoverForm(this::showPages,this::showHome,this::addCard,this::editBinder,this::deleteBinder);
        home=new SeratoImpactLook(marketplace,this::openBinder,this::createBinder,
                this::newSeller,this::switchOrEditSeller,this::toggleMode);
        screens.add(home.getMainPanel(),"HOME");
        screens.add(cover.getMainPanel(),"COVER");
        screens.add(pages.getMainPanel(),"PAGES");
        frame = new JFrame("Serato Impact - Digital Binder Marketplace");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(screens);

// Initial window size
        frame.setSize(1100, 850);

// Prevent the window from becoming too cramped
        frame.setMinimumSize(new Dimension(900, 700));

// Center the window on the screen
        frame.setLocationRelativeTo(null);

        showHome();
        frame.setVisible(true);
    }
    private void save() {
        try {repository.save(marketplace);}
        catch(IOException ex) {
            JOptionPane.showMessageDialog(frame,"Changes were made in memory but could NOT be saved:\n"+ex.getMessage(),
                    "Save Failed",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void showHome(){home.show(currentSeller,sellerMode);navigation.show(screens,"HOME");}
    private void openBinder(Binder binder){
        if (!sellerMode && !binder.isPublic()) return;
        if (sellerMode && !binder.getOwnerId().equals(currentSeller.getUserId())) return;
        currentBinder=binder;
        showCover();
    }
    private void showCover(){
        if(currentBinder==null){showHome();return;}
        cover.setBinder(currentBinder,marketplace.getOwner(currentBinder),sellerMode);
        navigation.show(screens,"COVER");
    }
    private void showPages(){
        if(currentBinder==null){showHome();return;}
        pages.setBinder(currentBinder,sellerMode);
        navigation.show(screens,"PAGES");
    }
    private void toggleMode(){
        sellerMode=!sellerMode;
        currentBinder=null;
        showHome();
    }
    private boolean canManage(){return sellerMode && currentBinder!=null &&
            currentBinder.getOwnerId().equals(currentSeller.getUserId());}
    private void createBinder(){
        if(!sellerMode)return;
        BinderEditorForm editor=BinderEditorForm.show(frame,null);
        if(editor==null)return;
        try {
            Binder b=marketplace.addBinder(currentSeller,editor.getName(),editor.getDescription(),editor.isPublic());
            save();openBinder(b);
        }catch(IllegalArgumentException ex){error(ex);}
    }
    private void editBinder(){
        if(!canManage())return;
        BinderEditorForm editor=BinderEditorForm.show(frame,currentBinder);
        if(editor==null)return;
        try{currentBinder.update(editor.getName(),editor.getDescription(),editor.isPublic());save();showCover();}
        catch(IllegalArgumentException ex){error(ex);}
    }
    private void deleteBinder(){
        if(!canManage())return;
        int confirmation=JOptionPane.showConfirmDialog(frame,
                "Delete binder '"+currentBinder.getName()+"' and all its cards? This cannot be undone.",
                "Delete Binder",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(confirmation!=JOptionPane.YES_OPTION)return;
        marketplace.removeBinder(currentBinder);currentBinder=null;save();showHome();
    }
    private void addCard(){
        if(!canManage())return;
        CardEditorForm editor=CardEditorForm.show(frame,null);
        if(editor==null)return;
        try{
            currentBinder.addCard(editor.createItem());
            save();showPages();
        }catch(IOException | IllegalArgumentException ex){error(ex);}
    }
    private void showCardDetails(InventoryItem item){
        if(currentBinder==null)return;
        User seller=marketplace.getOwner(currentBinder);
        Card card=item.getCard();
        JPanel details=new JPanel(new BorderLayout(14,10));
        ImageIcon picture=CardImage.load(card.getImagePath(),240,310);
        if(picture!=null) details.add(new JLabel(picture),BorderLayout.WEST);
        String price=item.isAvailableForSale()?"PHP "+item.getListing().getPrice().toPlainString():"NOT FOR SALE";
        String html="<html><div style='width:300px'>"
                +"<h2>"+CardImage.html(card.getName())+"</h2>"
                +"<p><b>Category:</b> "+CardImage.html(card.getCategory())+"<br>"
                +"<b>Set:</b> "+CardImage.html(card.getSetName())+"<br>"
                +"<b>Rarity:</b> "+CardImage.html(card.getRarity())+"<br>"
                +"<b>Condition:</b> "+CardImage.html(item.getCondition())+"<br>"
                +"<b>Stock:</b> "+item.getQuantity()+"<br>"
                +"<b>Price:</b> "+price+"</p>"
                +"<hr><p><b>Seller:</b> "+CardImage.html(seller==null?"Unknown":seller.getDisplayName())+"<br>"
                +"<b>Contact:</b> "+CardImage.html(seller==null?"Not provided":seller.getContact())+"</p>"
                +"<p>Contact the seller directly to inquire about purchasing.</p></div></html>";
        details.add(new JLabel(html),BorderLayout.CENTER);
        if(!sellerMode){
            JOptionPane.showMessageDialog(frame,details,"Card Details",JOptionPane.PLAIN_MESSAGE);
            return;
        }
        if(!canManage())return;
        String[] actions={"EDIT CARD","REMOVE CARD","CLOSE"};
        int option=JOptionPane.showOptionDialog(frame,details,"Card Details",
                JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,actions,actions[2]);
        if(option==0){
            CardEditorForm editor=CardEditorForm.show(frame,item);
            if(editor!=null){
                try{editor.updateItem(item);save();pages.refresh();}
                catch(IOException | IllegalArgumentException ex){error(ex);}
            }
        }else if(option==1){
            int confirmation=JOptionPane.showConfirmDialog(frame,
                    "Remove '"+card.getName()+"' from this binder?", "Remove Card",
                    JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(confirmation==JOptionPane.YES_OPTION){currentBinder.removeCard(item);save();pages.refresh();}
        }
    }
    private void newSeller(){
        if(!sellerMode)return;
        SellerEditorForm form=SellerEditorForm.show(frame,null);
        if(form==null)return;
        try {currentSeller=marketplace.addSeller(form.getName(),form.getContact());save();showHome();}
        catch(IllegalArgumentException ex){error(ex);}
    }
    private void switchOrEditSeller(){
        if(!sellerMode)return;
        String[] choices={"SWITCH SELLER","EDIT CURRENT PROFILE","CANCEL"};
        int action=JOptionPane.showOptionDialog(frame,
                "Current seller: "+currentSeller.getDisplayName(),"Seller Management",
                JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,choices,choices[0]);
        if(action==0){
            List<User> sellers=marketplace.getSellers();
            User selected=(User)JOptionPane.showInputDialog(frame,"Choose local seller profile:",
                    "Switch Seller",JOptionPane.QUESTION_MESSAGE,null,sellers.toArray(),currentSeller);
            if(selected!=null){currentSeller=selected;currentBinder=null;showHome();}
        }else if(action==1){
            SellerEditorForm form=SellerEditorForm.show(frame,currentSeller);
            if(form!=null){try{currentSeller.update(form.getName(),form.getContact());save();showHome();}
            catch(IllegalArgumentException ex){error(ex);}}
        }
    }
    private void error(Exception ex){JOptionPane.showMessageDialog(frame,ex.getMessage(),"Unable to complete action",JOptionPane.ERROR_MESSAGE);}
}
