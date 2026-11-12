import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BinderCoverForm {
    private JPanel coverMainPanel;
    private JPanel binderCoverPanel;
    private JLabel lblCoverImage;
    private JLabel lblCoverTitle;
    private JLabel lblCollectionName;
    private JButton btnOpenBinder;
    private JButton btnBackToCollection;
    private String selectedBinder = "Character";

    public BinderCoverForm(
            Consumer<String> onOpenBinder,
            Runnable onBack
    ) {
        btnOpenBinder.addActionListener(e ->
                onOpenBinder.accept(selectedBinder));

        btnBackToCollection.addActionListener(e ->
                onBack.run());
    }

    public void setBinder(String binderType) {
        selectedBinder = binderType;

        lblCoverTitle.setText(
                binderType.toUpperCase() + " CARD"
        );

        Color color;

        switch (binderType) {
            case "Equipment":
                color = new Color(148, 107, 49);
                break;

            case "Event":
                color = new Color(53, 83, 116);
                break;

            case "Support":
                color = new Color(101, 67, 90);
                break;

            default:
                color = new Color(35, 73, 56);
        }

        lblCoverImage.setBackground(color);
        lblCoverImage.setOpaque(true);
    }

    public JPanel getMainPanel() {
        return coverMainPanel;
    }
}
