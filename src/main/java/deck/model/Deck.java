package deck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Table(name = "DECK")
@Entity
public class Deck implements Serializable {
    private long id;

    private String backImageUrl;
    private int ownerId;
    private boolean visibleAsPublic;

    private String name;
    private String description;
    private int imagesOnCard;

    private Set<Image> images;

    private User user;


    public Deck(){

    }

    public Deck(long id, String backImageId, int ownerId, boolean visibleAsPublic) {
        this.id = id;
        this.backImageUrl = backImageId;
        this.ownerId = ownerId;
        this.visibleAsPublic = visibleAsPublic;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBackImageUrl() {
        return backImageUrl;
    }

    public void setBackImageUrl(String backImageURL) {
        this.backImageUrl = backImageURL;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deck", cascade = { CascadeType.ALL })
    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
