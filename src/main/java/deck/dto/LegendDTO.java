package deck.dto;

import java.util.List;

public class LegendDTO {
    Long deckId;
    Long textSize;
    List<List<LegendElementDto>> legendElementDtos;

    public LegendDTO() {
    }

    public Long getDeckId() {
        return deckId;
    }

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }

    public Long getTextSize() {
        return textSize;
    }

    public void setTextSize(Long textSize) {
        this.textSize = textSize;
    }

    public List<List<LegendElementDto>> getLegendElementDtos() {
        return legendElementDtos;
    }

    public void setLegendElementDtos(List<List<LegendElementDto>> legendElementDtos) {
        this.legendElementDtos = legendElementDtos;
    }
}
