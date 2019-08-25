package deck.dto;

public class CardImageDto {

    private String image;
    private int scale;
    private int angle;
    private int positionX;
    private int positionY;

    public CardImageDto() {
    }

    public CardImageDto(String image, int scale, int angle, int positionX, int positionY) {
        this.image = image;
        this.scale = scale;
        this.angle = angle;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
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
}
