package deck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Table(name = "LEGEND")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Legend implements Serializable {

    private long id;
    private Deck deck;
    private String name;
    private boolean isTuned;
    private Set<LegendElement> items;
    private int textSize = 18;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "legend", cascade = { CascadeType.ALL })
    public Set<LegendElement> getItems() {
        return items;
    }

    public void setItems(Set<LegendElement> items) {
        this.items = items;
    }

    public boolean getIsTuned() {
        return isTuned;
    }

    public void setIsTuned(boolean tuned) {
        isTuned = tuned;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
