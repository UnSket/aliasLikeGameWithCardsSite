package deck.dto;

public class ImageTextLegendDTO {

    long legendId;
    long imageId;
    String text;

    public ImageTextLegendDTO() {

    }

    public long getLegendId() {
        return legendId;
    }

    public void setLegendId(long legendId) {
        this.legendId = legendId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
