package deck.dto;

public class UpdateLegendDto {
    private LegendElementDto[] cards;
    private long legendId;
    private int textSize;

    public LegendElementDto[] getCards() {
        return cards;
    }

    public void setCards(LegendElementDto[] cards) {
        this.cards = cards;
    }

    public void setLegendId(long legendId) {
        this.legendId = legendId;
    }

    public long getLegendId() {
        return legendId;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
