import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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

    public static ImageIcon loadCover(String path, int targetW, int targetH) {
        if (path == null || path.trim().isEmpty()) return null;

        try {
            BufferedImage img = ImageIO.read(new File(path));

            if (img == null) return null;

            // Scale enough to completely cover the container
            double scale = Math.max(
                    (double) targetW / img.getWidth(),
                    (double) targetH / img.getHeight()
            );

            int scaledW = (int) Math.ceil(img.getWidth() * scale);
            int scaledH = (int) Math.ceil(img.getHeight() * scale);

            // Create an image with the exact container dimensions
            BufferedImage result = new BufferedImage(
                    targetW, targetH, BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = result.createGraphics();

            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );

            // Center and crop excess image portions
            int x = (targetW - scaledW) / 2;
            int y = (targetH - scaledH) / 2;

            g.drawImage(img, x, y, scaledW, scaledH, null);

            g.dispose();

            return new ImageIcon(result);

        } catch (IOException ex) {
            return null;
        }
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
