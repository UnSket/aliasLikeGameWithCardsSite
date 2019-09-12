package deck.dto;

public class UpdateLegendDto {
    private LegendElementDto[] legendElementDtos;
    private long deckId;

    public LegendElementDto[] getLegendElementDtos() {
        return legendElementDtos;
    }

    public void setLegendElementDtos(LegendElementDto[] legendElementDtos) {
        this.legendElementDtos = legendElementDtos;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }
}
