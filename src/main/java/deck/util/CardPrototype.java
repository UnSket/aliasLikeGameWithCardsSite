package deck.util;

import java.util.ArrayList;
import java.util.List;

public class CardPrototype {

    private List<CardImagePrototype> images = new ArrayList<>();

    public List<CardImagePrototype> getImages() {
        return images;
    }

    public void setImages(List<CardImagePrototype> images) {
        this.images = images;
    }
}
