package deck.image.processing;

import deck.config.StorageProperties;
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

import static deck.image.processing.RGBAColor.BACKGROUND_RADII;

@Service
public class ImageProcessing {

    private final long FILE_MAX_SIZE = 20_000_000;
    private final int IMAGE_MAX_SIZE = 168;
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");
    private static final RGBAColor pngBgColor = new RGBAColor(0);

    static {
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

    private BufferedImage scaleDownImage(BufferedImage source) {

        int width = source.getWidth();
        int height = source.getHeight();

        int minax = Math.min(width, height);
        if (minax < IMAGE_MAX_SIZE * 2) {
            return source;
        }
        int scaleCoef = minax / IMAGE_MAX_SIZE - 1;

        int newWidth = width / scaleCoef;
        int newHeight = height / scaleCoef;

        BufferedImage result = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.setRGB(i, j, source.getRGB(i * scaleCoef, j * scaleCoef));
            }
        }
        return result;
    }

    private double calculatePixelWeight(int width, int height, int i, int j){
        float centX = width/2.0f;
        float centY = height/2.0f;
        float diffX = Math.abs(centX-i);
        float diffY = Math.abs(centY-j);
        double v = Math.sqrt(diffX*diffX+diffY*diffY);
        return Math.log10(v)+4;
    };

    public String cleanUpBackGround(File f, String uuid, boolean needBgCleanUp) {
        BufferedImage img;
        BufferedImage result;
        HashMap<RGBAColor, Double> decompressedRgbaOccurences = new HashMap<>();

        try {
            img = ImageIO.read(f);
            img = scaleDownImage(img);
            int width = img.getWidth();
            int height = img.getHeight();
            result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgbaBytes = img.getRGB(i, j);
                    RGBAColor color = new RGBAColor(rgbaBytes);
                    int modifier = 1;
                    if(color.getA()>255){
                        color.setB(255);
                        color.setR(255);
                        color.setG(255);
                        color.setA(255);
                        modifier = 3;
                    }
                    if(color.getB()+color.getR()+color.getG()>700){
                        modifier = 2;
                    }
                    double metrics = calculatePixelWeight(width, height, i, j) * modifier;
                    if (decompressedRgbaOccurences.containsKey(color)) {
                        decompressedRgbaOccurences.put(color,
                                decompressedRgbaOccurences.get(color) + metrics);
                    } else {
                        decompressedRgbaOccurences.put(color, metrics);
                    }
                }
            }

            Optional<Map.Entry<RGBAColor, Double>> max =
                    decompressedRgbaOccurences.entrySet()
                            .stream()
                            .max(Comparator.comparingDouble(Map.Entry::getValue));

            if (!max.isPresent()) {
                return "";
            }
            Map.Entry<RGBAColor, Double> rgbaColorLongEntry = max.get();
            RGBAColor bgColor = rgbaColorLongEntry.getKey();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgbaBytes = img.getRGB(i, j);
                    RGBAColor color = new RGBAColor(rgbaBytes);
                    if (needBgCleanUp && color.calculateCartesianDistance(bgColor) < BACKGROUND_RADII) {
                        color.setColor(pngBgColor);
                    }
                    rgbaBytes = color.convertTo4ByteFormat();
                    result.setRGB(i, j, rgbaBytes);
                }
            }

            String format = ".png";
            String filename = StringUtils.cleanPath(uuid + format);

            File results = new File(this.rootLocation.resolve(filename).toUri());
            ImageIO.write(result, "png", results);

            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
