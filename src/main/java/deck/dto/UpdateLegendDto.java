package deck.dto;

public class UpdateLegendDto {
    private LegendElementDto[] items;
    private long deckId;

    public LegendElementDto[] getItems() {
        return items;
    }

    public void setItems(LegendElementDto[] items) {
        this.items = items;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }
}
