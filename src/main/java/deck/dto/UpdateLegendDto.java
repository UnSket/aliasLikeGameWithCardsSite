package deck.dto;

public class UpdateLegendDto {
    private LegendElementDto[] cards;
    private long deckId;
    private int textSize;

    public LegendElementDto[] getCards() {
        return cards;
    }

    public void setCards(LegendElementDto[] cards) {
        this.cards = cards;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
