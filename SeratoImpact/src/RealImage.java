
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RealImage {

    private final String path;
    private BufferedImage image;

    public RealImage(String path) {
        this.path = path;
    }

    public BufferedImage getImage() throws IOException {

        if (image == null) {
            System.out.println("Loading full-resolution image...");
            image = CardImage.loadFull(path);
        }

        return image;
    }
}
