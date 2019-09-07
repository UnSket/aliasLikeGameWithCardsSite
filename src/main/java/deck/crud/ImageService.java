package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.image.generation.CardConfigurationProcessor;
import deck.model.Deck;
import deck.model.Image;
import deck.repository.DeckRepository;
import deck.repository.ImageRepository;
import deck.storage.ImageStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {


    private final ImageRepository imageRepository;
    private final CardConfigurationProcessor cardConfigurationProcessor;
    private final CardsService cardsService;
    private final DeckRepository deckRepository;
    private final DeckService deckService;

    @Autowired
    public ImageService(ImageRepository imageRepository,
                        CardConfigurationProcessor cardConfigurationProcessor,
                        CardsService cardsService,
                        DeckRepository deckRepository,
                        DeckService deckService) {

        this.imageRepository = imageRepository;
        this.cardConfigurationProcessor = cardConfigurationProcessor;
        this.cardsService = cardsService;
        this.deckRepository = deckRepository;
        this.deckService = deckService;
    }

    @Transactional
    public Image submitNewAndGet(String imageUrl, Long deckId) {
        Deck deck = deckService.getById(deckId);
        if (deck.getImagesRequired() == 0) {
            throw new ImageStorageException("too many cards");
        }
        Image image = new Image(imageUrl, deck);
        deck.getImages().add(image);
        deck.setImagesRequired(deck.getImagesRequired() - 1);

        deckRepository.save(deck);
        if (deck.getImagesRequired() == 0) {
            updateDeckRequiredCardsCountData(deck);
        }
        return image;
    }

    private void updateDeckRequiredCardsCountData(Deck deck) {
        cardsService.generateCardsForDeck(deck);
        System.out.println("cards generated for deck №" + deck.getId());
        deckRepository.save(deck);
    }

    public Image submitNewAndGet(String imageUrl) {
        Image image = new Image();
        image.setUrl(imageUrl);
        return imageRepository.save(image);
    }

    public Image submitImageText(long imageId, String imageText) {
        Optional<Image> byId = imageRepository.findById(imageId);
        if(!byId.isPresent()){
            throw new ResourceNotFoundException();
        }
        Image image = byId.get();
        image.setText(imageText);
        return imageRepository.save(image);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public Image getById(long id) {
        Optional<Image> byId = imageRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return byId.orElseGet(Image::new);
    }

    public Image updateImageAndGet(Long imageId, String newUrl) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image with id " + imageId + " not found"));
        image.setUrl(newUrl);
        return imageRepository.save(image);
    }
}
