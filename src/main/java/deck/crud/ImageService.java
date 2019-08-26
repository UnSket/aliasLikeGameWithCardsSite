package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.image.generation.CardConfigurationProcessor;
import deck.model.Deck;
import deck.model.Image;
import deck.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {


    private final ImageRepository imageRepository;
    private final CardConfigurationProcessor cardConfigurationProcessor;
    private final CardsService cardsService;

    @Autowired
    public ImageService(ImageRepository imageRepository, CardConfigurationProcessor cardConfigurationProcessor, CardsService cardsService) {
        this.imageRepository = imageRepository;
        this.cardConfigurationProcessor = cardConfigurationProcessor;
        this.cardsService = cardsService;
    }

    public Image submitNewAndGet(String imageUrl, Deck deck){
        Image image = new Image(imageUrl, deck);
        int deckSize = deck.getImages().size();
        int imagesOnCard = deck.getImagesOnCard();
        int expectedCardCount = cardConfigurationProcessor.getExpectedImagesCountByImagesOnCard(imagesOnCard);
        if(deckSize >= expectedCardCount){
            cardsService.generateCardsForDeck(deck);
            System.out.println("cards generated for deck №" + deck.getId());
        }
        return imageRepository.save(image);
    }

    public Image submitNewAndGet(String imageUrl){
        Image image = new Image();
        image.setUrl(imageUrl);
        return imageRepository.save(image);
    }

    public List<Image> findAll(){
        return imageRepository.findAll();
    }

    public Image getById(long id){
        Optional<Image> byId = imageRepository.findById(id);
        if(!byId.isPresent()){
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
