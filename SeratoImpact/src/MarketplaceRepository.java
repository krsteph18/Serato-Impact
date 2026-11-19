import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class MarketplaceRepository {
    private final Path file;
    public MarketplaceRepository() {
        file = Path.of(System.getProperty("user.home"), ".serato-impact", "marketplace.dat");
    }
    public Path getFile() { return file; }
    public Marketplace load() {
        if (!Files.exists(file)) return new Marketplace();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            Object value = in.readObject();
            if (value instanceof Marketplace) return (Marketplace) value;
            throw new IOException("Unknown data format");
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Cannot read saved data from " + file + ". " + e.getMessage(), e);
        }
    }
    public void save(Marketplace marketplace) throws IOException {
        Files.createDirectories(file.getParent());
        Path temporary = Files.createTempFile(file.getParent(), "marketplace-", ".tmp");
        try {
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(temporary))) {
                out.writeObject(marketplace);
            }
            try {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally { Files.deleteIfExists(temporary); }
    }
}
