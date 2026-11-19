import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/** Images are optional; thumbnails are read from local files. */
public final class CardImage {
    private CardImage() { }
    public static ImageIcon load(String path, int maxW, int maxH) {
        if (path == null || path.trim().isEmpty()) return null;
        try {
            BufferedImage img = ImageIO.read(new File(path));
            if (img == null) return null;
            double scale = Math.min((double) maxW / img.getWidth(), (double) maxH / img.getHeight());
            scale = Math.min(1.0, scale);
            int width = Math.max(1, (int) (img.getWidth() * scale));
            int height = Math.max(1, (int) (img.getHeight() * scale));
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException ex) { return null; }
    }

    public static String importImage(String path) throws IOException {
        if (path == null || path.trim().isEmpty()) return "";
        Path source = Path.of(path.trim()).toAbsolutePath().normalize();
        if (!Files.isRegularFile(source)) throw new IOException("Card image does not exist: " + source);
        if (ImageIO.read(source.toFile()) == null) throw new IOException("Image must be JPEG or PNG.");
        Path destDir = Path.of(System.getProperty("user.home"), ".serato-impact", "images");
        if (source.startsWith(destDir)) return source.toString();
        Files.createDirectories(destDir);
        String fn = source.getFileName().toString().toLowerCase();
        String ext = fn.endsWith(".png") ? ".png" : fn.endsWith(".jpeg") ? ".jpeg" : ".jpg";
        Path dest = destDir.resolve(UUID.randomUUID().toString() + ext);
        Files.copy(source, dest);
        return dest.toString();
    }
    public static String html(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
