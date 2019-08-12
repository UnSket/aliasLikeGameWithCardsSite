package deck.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "CARD")
@Entity
public class Card implements Serializable {

    private int id;

    private int deckId;

    public Card(){

    }

    public Card(int id, int deckId) {
        this.id = id;
        this.deckId = deckId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }
}
