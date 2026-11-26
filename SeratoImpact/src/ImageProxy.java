
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageProxy {

    private final String path;
    private final int thumbnailWidth;
    private final int thumbnailHeight;

    private ImageIcon thumbnail;
    private RealImage realImage;

    public ImageProxy(
            String path,
            int thumbnailWidth,
            int thumbnailHeight
    ) {
        this.path = path;
        this.thumbnailWidth = thumbnailWidth;
        this.thumbnailHeight = thumbnailHeight;
    }

    // Loads only the low-resolution thumbnail
    public ImageIcon getThumbnail() {

        if (thumbnail == null) {
            System.out.println("Loading thumbnail...");

            thumbnail = CardImage.loadCover(
                    path,
                    thumbnailWidth,
                    thumbnailHeight
            );
        }

        return thumbnail;
    }

    // Loads the full-resolution image only when requested
    public BufferedImage getFullImage() throws IOException {

        if (realImage == null) {
            realImage = new RealImage(path);
        }

        return realImage.getImage();
    }
}
