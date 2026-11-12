import javax.swing.*;
import java.util.function.Consumer;

public class SeratoImpactLook {
    private JPanel mainPanel;
    private JPanel homePanel;
    private JLabel lblMainTitle;
    private JTextField txtSearch;
    private JLabel lblMyBinders;
    private JPanel binderGridPanel;
    private JButton btnCharacter;
    private JButton btnEquipment;
    private JButton btnEvent;
    private JButton btnSupport;
    public SeratoImpactLook(Consumer<String> onBinderClick) {

        btnCharacter.addActionListener(e ->
                onBinderClick.accept("Character"));

        btnEquipment.addActionListener(e ->
                onBinderClick.accept("Equipment"));

        btnEvent.addActionListener(e ->
                onBinderClick.accept("Event"));

        btnSupport.addActionListener(e ->
                onBinderClick.accept("Support"));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
