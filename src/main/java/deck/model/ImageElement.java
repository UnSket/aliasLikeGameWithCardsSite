package deck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;


@Table(name = "IMAGE")
@Entity
public class ImageElement implements Serializable {

    private long id;

    private String text;

    private String url;

    private Deck deck;

    public ImageElement(){

    }

    public ImageElement(String url, Deck deck) {
        this.url = url;
        this.deck = deck;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "DECK_ID")
    @JsonIgnore
    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
