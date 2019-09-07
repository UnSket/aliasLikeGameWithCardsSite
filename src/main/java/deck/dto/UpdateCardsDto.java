package deck.dto;

import deck.model.CardImage;


public class UpdateCardsDto {
    private CardImage[][] cards;
    private long deckId;

    public CardImage[][] getCards() {
        return cards;
    }

    public void setCards(CardImage[][] cards) {
        this.cards = cards;
    }

    public long getDeckId() {
        return deckId;
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }
}
