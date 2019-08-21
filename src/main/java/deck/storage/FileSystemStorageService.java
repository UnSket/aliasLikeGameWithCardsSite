package deck.storage;

import deck.config.StorageProperties;
import deck.model.ImageMeta;
import deck.repository.ImageMetaRepository;
import deck.util.ImageProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private ImageMetaRepository imageMetaRepository;
    private ImageProcessing imageProcessing;

    @Autowired
    public FileSystemStorageService(StorageProperties properties,
                                    ImageMetaRepository imageMetaRepository,
                                    ImageProcessing imageProcessing) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.imageMetaRepository = imageMetaRepository;
        this.imageProcessing = imageProcessing;
    }

    @Override
    public String store(MultipartFile file) {
        imageProcessing.validateImage(file);
        UUID uuid = UUID.randomUUID();

        String contentType = file.getContentType();
        String[] split = Objects.requireNonNull(contentType).split("/");
        String format = "." + split[1];

        String filename = StringUtils.cleanPath(uuid.toString() + format);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ImageStorageException("Failed to store file ");
        }

        try {
            String s = imageProcessing.cleanUpBackGround(this.rootLocation.resolve(filename).toFile(), uuid.toString());
            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }

        ImageMeta meta = new ImageMeta();
        meta.setLink(filename);
        return uuid.toString();

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new ImageStorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ImageStorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new ImageStorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new ImageStorageException("Could not initialize storage", e);
        }
    }
}
