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
import java.util.*;

import static deck.util.RGBAColor.BACKGROUND_RADII;

@Service
public class ImageProcessing {

    private final long FILE_MAX_SIZE = 20_000_000;
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
    private static final RGBAColor pngBgColor = new RGBAColor(0);

    static{
        pngBgColor.setA(255);//blue
        pngBgColor.setB(0);
        pngBgColor.setR(255);
        pngBgColor.setG(255);
    }

    private final Path rootLocation;

    @Autowired
    public ImageProcessing(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
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

    public String cleanUpBackGround(File f, String uuid) {
        BufferedImage img;
        BufferedImage result;
        HashMap<RGBAColor, Long> decompressedRgbaOccurences = new HashMap<>();

        try {
            img = ImageIO.read(f);
            int width = img.getWidth();
            int height = img.getHeight();

            result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgbaBytes = img.getRGB(i, j);
                    RGBAColor color = new RGBAColor(rgbaBytes);
                    if(decompressedRgbaOccurences.containsKey(color)){
                        decompressedRgbaOccurences.put(color,
                                decompressedRgbaOccurences.get(color)+1);
                    }else{
                        decompressedRgbaOccurences.put(color, 1L);
                    }
                }
            }

            Optional<Map.Entry<RGBAColor, Long>> max =
                    decompressedRgbaOccurences.entrySet()
                            .stream()
                            .max(Comparator.comparingLong(Map.Entry::getValue));

            if(!max.isPresent()){
                return "";
            }
            Map.Entry<RGBAColor, Long> rgbaColorLongEntry = max.get();
            RGBAColor bgColor = rgbaColorLongEntry.getKey();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgbaBytes = img.getRGB(i, j);
                    RGBAColor color = new RGBAColor(rgbaBytes);
                    if(color.calculateCartesianDistance(bgColor)<BACKGROUND_RADII){
                       color.setColor(pngBgColor);
                    }
                    rgbaBytes = color.convertTo4ByteFormat();
                    result.setRGB(i, j, rgbaBytes);
                }
            }

            String format = ".png";
            String filename = StringUtils.cleanPath(uuid + format);

            ImageMeta meta = new ImageMeta();
            meta.setLink(filename);
            File results = new File(this.rootLocation.resolve(filename).toUri());
            ImageIO.write(result, "png", results);

            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
