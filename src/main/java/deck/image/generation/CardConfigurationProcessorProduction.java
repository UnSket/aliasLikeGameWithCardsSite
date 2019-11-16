package deck.image.generation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Profile("default")
public class CardConfigurationProcessorProduction implements CardConfigurationProcessor {

    private final ResourceLoader resourceLoader;

    private List<CardPrototype> perFiveConfig = new ArrayList<>();
    private List<CardPrototype> perSixConfig = new ArrayList<>();
    private List<CardPrototype> perEightConfig = new ArrayList<>();

    @Autowired
    public CardConfigurationProcessorProduction(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    private void enrichCaches() throws IOException {
        Path resource5 = null;
        Path resource6 = null;
        Path resource8 = null;

        try {
            resource5 = Paths.get("/home/jelastic/conf/5");
            resource6 = Paths.get("/home/jelastic/conf/6");
            resource8 = Paths.get("/home/jelastic/conf/8");
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        this.enrichType(resource5, perFiveConfig);
        enrichType(resource6, perSixConfig);
        enrichType(resource8, perEightConfig);

        System.out.println("Card caches configuration enriched");
    }

    public List<CardPrototype> getPerFiveConfig() {
        return perFiveConfig;
    }

    public List<CardPrototype> getPerSixConfig() {
        return perSixConfig;
    }

    public List<CardPrototype> getPerEightConfig() {
        return perEightConfig;
    }

    public int getExpectedImagesCountByImagesOnCard(Integer imagesOnCard) {
        if(imagesOnCard == null){
            throw new CardGenerationUnavailable("NPE: 5,6,8 for number of images on one card");
        }
        switch (imagesOnCard) {
            case 5:
                return perFiveConfig.size();
            case 6:
                return perSixConfig.size();
            case 8:
                return perEightConfig.size();
            default:
                throw new CardGenerationUnavailable("available values: 5,6,8 for number of images on one card");
        }
    }

    private void enrichType(Path resource, List<CardPrototype> perXConfig) {
        try {
            Stream paths = Files.walk(resource);

            try {
                paths.sorted().filter((x) -> {
                    return Files.isRegularFile((Path) x, new LinkOption[0]);
                }).forEach((z) -> {
                    CardPrototype card = this.enrichCard((Path) z);
                    perXConfig.add(card);
                });
            } catch (Throwable var7) {
                if (paths != null) {
                    try {
                        paths.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (paths != null) {
                paths.close();
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    private CardPrototype enrichCard(Path path) {
        File file = path.toFile();

        CardPrototype card = new CardPrototype();
        try {
            FileInputStream fstream = new FileInputStream(file);

            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                String clearString = cleanUpString(strLine);
                String[] params = clearString.split(" ");
                CardImagePrototype prototype = new CardImagePrototype();
                prototype.setX(Integer.parseInt(params[0]));
                prototype.setY(Integer.parseInt(params[1]));
                prototype.setRotationAngleMillirad(Integer.parseInt(params[2]));
                prototype.setScaleFactor(Integer.parseInt(params[3]));
                prototype.setImageId(Integer.parseInt(params[4]));
                card.getImages().add(prototype);

            }

            fstream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return card;
    }

    private String cleanUpString(String sourceData) {
        return sourceData.replaceAll("rotate\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("http://localhost/spotIt/projects/example/", " ")
                .replaceAll(" http://localhost/spotit/projects/test5/", " ")
                .replaceAll(" http://localhost/spotit/projects/test/", " ")
                .replaceAll(".png", "")
                .replaceAll("deg", "")
                .replaceAll(" {2}", " ");
    }

}
