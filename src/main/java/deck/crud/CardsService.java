package deck.crud;

import deck.dto.CardImageDto;
import deck.model.Card;
import deck.model.CardImage;
import deck.model.Deck;
import deck.model.Image;
import deck.repository.CardImageRepository;
import deck.repository.CardRepository;
import deck.util.CardConfigurationProcessor;
import deck.util.CardGenerationUnavailable;
import deck.util.CardImagePrototype;
import deck.util.CardPrototype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CardsService {

    private final CardConfigurationProcessor cardConfigurationProcessor;
    private final CardRepository cardRepository;
    private final CardImageRepository cardImageRepository;

    @Autowired
    public CardsService(CardConfigurationProcessor cardConfigurationProcessor, CardRepository cardRepository, CardImageRepository cardImageRepository) {
        this.cardConfigurationProcessor = cardConfigurationProcessor;
        this.cardRepository = cardRepository;
        this.cardImageRepository = cardImageRepository;
    }

    public void generateCardsForDeck(Deck deck) {
        int imagesOnCard = deck.getImagesOnCard();
        List<CardPrototype> prototypes = null;
        switch (imagesOnCard) {
            case 5:
                prototypes = cardConfigurationProcessor.getPerFiveConfig();
                break;
            case 6:
                prototypes = cardConfigurationProcessor.getPerSixConfig();
                break;
            case 8:
                prototypes = cardConfigurationProcessor.getPerEightConfig();
                break;
        }

        if (Objects.requireNonNull(prototypes).size() > deck.getImages().size()) {
            throw new CardGenerationUnavailable("not enough images for cards generation");
        }

        List<Card> persistedCards = new ArrayList<>();
        for (int i = 0; i < prototypes.size(); i++) {
            Card card = new Card();
            card.setDeckId(deck.getId());
            persistedCards.add(card);
        }
        List<Card> cards = cardRepository.saveAll(persistedCards);

        List<Image> images = deck.getImages();
        for (int cardNumber = 0; cardNumber < cards.size(); cardNumber++) {
            Card card = cards.get(cardNumber);
            CardPrototype cardPrototype = prototypes.get(cardNumber);

            List<CardImage> cardImages = new ArrayList<>();
            for (int imageOnCardNumber = 0; imageOnCardNumber < cardPrototype.getImages().size(); imageOnCardNumber++) {
                CardImagePrototype cardImagePrototype = cardPrototype.getImages().get(imageOnCardNumber);

                CardImage cardImage = new CardImage();
                cardImage.setCardId(card.getId());
                cardImage.setImageId(images.get(cardImagePrototype.getImageId()).getId());
                cardImage.setPositionX(cardImagePrototype.getX());
                cardImage.setPositionY(cardImagePrototype.getY());
                cardImage.setRotationAngleMillirad(cardImagePrototype.getRotationAngleMillirad());
                cardImage.setScaleFactor(cardImagePrototype.getScaleFactor());
                cardImages.add(cardImage);
            }
            cardImageRepository.saveAll(cardImages);

        }

    }

    public List<List<CardImageDto>> getDeckData() {
        return null;
    }
}
