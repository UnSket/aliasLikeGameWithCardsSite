package deck.dto;

public class LegendElementDto {

    private long id;
    private LegendSourceType legendSourceType;
    private String source;
    private int positionX;
    private int positionY;
    private int cardNumber;

    public LegendElementDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LegendSourceType getLegendSourceType() {
        return legendSourceType;
    }

    public void setLegendSourceType(LegendSourceType legendSourceType) {
        this.legendSourceType = legendSourceType;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public enum LegendSourceType{
        TEXT,
        IMAGE
    }
}
