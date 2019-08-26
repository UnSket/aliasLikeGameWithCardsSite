package deck.image.generation;

public class CardImagePrototype {

    private int x;

    private int y;

    private int imageId;

    private int scaleFactor;

    private int rotationAngleMillirad;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public int getRotationAngleMillirad() {
        return rotationAngleMillirad;
    }

    public void setRotationAngleMillirad(int rotationAngleMillirad) {
        this.rotationAngleMillirad = rotationAngleMillirad;
    }
}
