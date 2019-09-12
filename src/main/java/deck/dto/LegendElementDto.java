package deck.dto;

public class LegendElementDto {

    private String content;
    private LegendSourceType legendSourceType;

    private int positionX;
    private int positionY;
    private int cardNumber;

    public LegendElementDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public enum LegendSourceType{
        TEXT,
        IMAGE
    }
}
