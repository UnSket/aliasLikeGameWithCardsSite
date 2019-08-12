package deck.model;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "CARD_IMAGE")
@Entity
public class CardImage implements Serializable {

    private int id;

    private int cardId;

    private int imageId;

    private int offsetHeight;

    private int offsetWidth;

    private int rotationAngleMillirad;

    public CardImage(){

    }

    public CardImage(int id, int cardId, int imageId, int offsetHeight, int offsetWidth, int rotationAngleMillirad) {
        this.id = id;
        this.cardId = cardId;
        this.imageId = imageId;
        this.offsetHeight = offsetHeight;
        this.offsetWidth = offsetWidth;
        this.rotationAngleMillirad = rotationAngleMillirad;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public int getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetWidth(int offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public int getRotationAngleMillirad() {
        return rotationAngleMillirad;
    }

    public void setRotationAngleMillirad(int rotationAngleMillirad) {
        this.rotationAngleMillirad = rotationAngleMillirad;
    }
}
