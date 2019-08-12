package deck.dto;

public class CardImageDto {

    private String image;
    private float scale;
    private float angle;
    private float positionX;
    private float positionY;

    public CardImageDto(String image, float scale, float angle, float positionX, float positionY) {
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getPositionX() {
        return positionX;
    }

    public void setPositionX(float positionX) {
        this.positionX = positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }
}
