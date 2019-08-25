package deck.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "CARD")
@Entity
public class Card implements Serializable {

    private long id;

    private long deckId;

    public Card() {

    }

    public Card(int id, int deckId) {
        this.id = id;
        this.deckId = deckId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }
}
