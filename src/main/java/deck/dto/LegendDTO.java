package deck.dto;

import java.util.List;

public class LegendDTO {
    Long deckId;
    int textSize;
    List<List<LegendElementDto>> items;

    public LegendDTO() {
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public List<List<LegendElementDto>> getItems() {
        return items;
    }

    public void setItems(List<List<LegendElementDto>> items) {
        this.items = items;
    }
}
