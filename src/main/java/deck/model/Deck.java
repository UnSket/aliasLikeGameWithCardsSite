package deck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Table(name = "DECK")
@Entity
public class Deck implements Serializable {
    private long id;

    private String backsideKey;
    private User owner;
    private boolean visibleAsPublic;

    private String name;
    private String description;
    private int imagesOnCard;

    private Set<Image> images;

    public Deck(){}

    public Deck(long id, User owner, boolean visibleAsPublic) {
        this.id = id;
        this.owner = owner;
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

    public String getBacksideKey() {
        return backsideKey;
    }

    public void setBacksideKey(String backsideKey) {
        this.backsideKey = backsideKey;
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
    @JoinColumn(name = "OWNER")
    @JsonIgnore
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
