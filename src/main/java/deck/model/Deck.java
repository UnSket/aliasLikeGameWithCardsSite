package deck.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "DECK")
@Entity
public class Deck implements Serializable {

    private long id;

    //TODO: URL
    private int backImageId;
    private int ownerId;
    private boolean visibleAsPublic;

    private String name;
    private String description;
    private int imagesOnCard;

    public Deck(){

    }

    public Deck(long id, int backImageId, int ownerId, boolean visibleAsPublic) {
        this.id = id;
        this.backImageId = backImageId;
        this.ownerId = ownerId;
        this.visibleAsPublic = visibleAsPublic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBackImageId() {
        return backImageId;
    }

    public void setBackImageId(int backImageId) {
        this.backImageId = backImageId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isVisibleAsPublic() {
        return visibleAsPublic;
    }

    public void setVisibleAsPublic(boolean visibleAsPublic) {
        this.visibleAsPublic = visibleAsPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImagesOnCard() {
        return imagesOnCard;
    }

    public void setImagesOnCard(int imagesOnCard) {
        this.imagesOnCard = imagesOnCard;
    }
}
