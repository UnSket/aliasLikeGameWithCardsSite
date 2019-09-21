package deck.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import deck.dto.LegendElementDto;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "LEGEND_ELEMENT")
@Entity
public class LegendElement implements Serializable {

    private long id;

    private Deck deck;

    private String content;

    private Long imageId;

    @Enumerated(EnumType.STRING)
    private LegendElementDto.LegendSourceType legendSourceType;

    private int positionX;
    private int positionY;
    private int cardNumber;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LegendElementDto.LegendSourceType getLegendSourceType() {
        return legendSourceType;
    }

    public void setLegendSourceType(LegendElementDto.LegendSourceType legendSourceType) {
        this.legendSourceType = legendSourceType;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
