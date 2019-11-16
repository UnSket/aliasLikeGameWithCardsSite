package deck.image.generation;

import java.util.List;

public interface CardConfigurationProcessor {
    public List<CardPrototype> getPerFiveConfig();
    public List<CardPrototype> getPerSixConfig();

    public List<CardPrototype> getPerEightConfig();

    public int getExpectedImagesCountByImagesOnCard(Integer imagesOnCard);

}
