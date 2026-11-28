import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

public class RealImage {

    private final String path;
    private BufferedImage image;

    public RealImage(String path) {
        this.path = path;
    }

    public BufferedImage getImage() throws IOException {

        if (image == null) {
            System.out.println("Loading full-resolution image...");
            image = ImageIO.read(new File(path));
        }

        return image;
    }
}