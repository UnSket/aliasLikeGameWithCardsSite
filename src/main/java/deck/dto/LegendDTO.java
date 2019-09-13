package deck.dto;

import java.util.List;

public class LegendDTO {
    Long deckId;
    int textSize;
    List<List<LegendElementDto>> cards;

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

    public List<List<LegendElementDto>> getCards() {
        return cards;
    }

    public void setCards(List<List<LegendElementDto>> cards) {
        this.cards = cards;
    }
}
