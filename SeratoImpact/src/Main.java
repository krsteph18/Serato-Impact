import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame(
                    "Genshin TCG Binder Management System"
            );

            CardLayout cardLayout = new CardLayout();

            JPanel screenContainer =
                    new JPanel(cardLayout);

            // STEP 1: Create Binder Pages
            BinderPagesForm pagesForm =
                    new BinderPagesForm(() -> {
                        cardLayout.show(
                                screenContainer, "COVER"
                        );
                    });

            // STEP 2: Create Binder Cover
            BinderCoverForm coverForm =
                    new BinderCoverForm(
                            binderType -> {

                                pagesForm.setBinder(binderType);

                                cardLayout.show(
                                        screenContainer, "PAGES"
                                );
                            },

                            () -> {
                                cardLayout.show(
                                        screenContainer, "HOME"
                                );
                            }
                    );

            // STEP 3: Create Main Collection
            SeratoImpactLook homeForm =
                    new SeratoImpactLook(
                            binderType -> {

                                coverForm.setBinder(binderType);

                                cardLayout.show(
                                        screenContainer, "COVER"
                                );
                            }
                    );

            // Add all screens
            screenContainer.add(
                    homeForm.getMainPanel(), "HOME"
            );

            screenContainer.add(
                    coverForm.getMainPanel(), "COVER"
            );

            screenContainer.add(
                    pagesForm.getMainPanel(), "PAGES"
            );

            // Start with home screen
            cardLayout.show(screenContainer, "HOME");

            frame.setContentPane(screenContainer);

            frame.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            frame.setSize(950, 780);
            frame.setMinimumSize(
                    new Dimension(650, 600)
            );

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
