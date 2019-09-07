package deck.image.generation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CardConfigurationProcessor {

    private final ResourceLoader resourceLoader;

    private List<CardPrototype> perFiveConfig = new ArrayList<>();
    private List<CardPrototype> perSixConfig = new ArrayList<>();
    private List<CardPrototype> perEightConfig = new ArrayList<>();

    @Autowired
    public CardConfigurationProcessor(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    private void enrichCaches() throws IOException {
        Resource resource5 = resourceLoader.getResource("classpath:card_configs/5");
        Resource resource6 = resourceLoader.getResource("classpath:card_configs/6");
        Resource resource8 = resourceLoader.getResource("classpath:card_configs/8");

        enrichType(resource5, perFiveConfig);
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

    private void enrichType(Resource resource, List<CardPrototype> perXConfig) {

        try (Stream<Path> paths = Files.walk(resource.getFile().toPath())) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(z -> {
                        CardPrototype card = enrichCard(z);


                        //TODO:remove this if want all images
                        if(perXConfig.size()==0) {
                            perXConfig.add(card);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
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

                //TODO:remove this if want all images
                break;
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
