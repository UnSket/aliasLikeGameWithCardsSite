package deck.util;

import deck.config.StorageProperties;
import deck.model.ImageMeta;
import deck.storage.ImageStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ImageProcessing {

    private final long FILE_MAX_SIZE = 20_000_000;
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");


    private final Path rootLocation;

    @Autowired
    public ImageProcessing(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public String cleanUpBackGround(File f, String uuid) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(f);
            int width = img.getWidth();
            int height = img.getHeight();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < i && j < height; j++) {
                    int rgbaBytes = img.getRGB(i, j);
                    int b = (rgbaBytes >> 24) & 0xff;
                    int r = (rgbaBytes >> 16) & 0xff;
                    int g = (rgbaBytes >> 8) & 0xff;
                    int a = rgbaBytes & 0xff;

                    a = 255 - a;
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    rgbaBytes = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(i, j, rgbaBytes);
                }
            }
            String format = ".png";
            String filename = StringUtils.cleanPath(uuid.toString() + format);

            ImageMeta meta = new ImageMeta();
            meta.setLink(filename);

            File results = new File(this.rootLocation.resolve(filename).toUri());
            ImageIO.write(img, "png", results);


            return uuid.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void validateImage(MultipartFile file) throws ImageStorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            throw new ImageStorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            // This is a security check
            throw new ImageStorageException(
                    "Cannot store file with relative path outside current directory "
                            + filename);
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            // This is a security check
            throw new ImageStorageException(
                    "Cannot store file with size more than " + FILE_MAX_SIZE +
                            " Current size " + file.getSize());
        }

        String fileContentType = file.getContentType();
        if (!contentTypes.contains(fileContentType)) {
            throw new ImageStorageException(
                    "Cannot store file in format that differs from listed:" + contentTypes.toString());
        }
    }
}
