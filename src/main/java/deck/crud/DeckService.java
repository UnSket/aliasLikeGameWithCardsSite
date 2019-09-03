package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.dto.DeckDTO;
import deck.image.generation.CardConfigurationProcessor;
import deck.model.CardImage;
import deck.model.Project;
import deck.model.User;
import deck.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeckService {

    private final CardsService cardsService;
    private final DeckRepository deckRepository;
    private final UserService userService;
    private final CardConfigurationProcessor cardConfigurationProcessor;

    @Autowired
    public DeckService(CardsService cardsService,
                       DeckRepository deckRepository,
                       UserService userService,
                       CardConfigurationProcessor cardConfigurationProcessor) {
        this.cardsService = cardsService;
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.cardConfigurationProcessor = cardConfigurationProcessor;
    }

    public Project submitNewDeck(DeckDTO deckDto) {
        User currentUser = userService.getCurrentUser();
        Project deck = new Project();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        deck.setImagesOnCard(deckDto.getImagesOnCard());
        deck.setOwner(currentUser);

        int expectedCardCount = cardConfigurationProcessor.getExpectedImagesCountByImagesOnCard(deck.getImagesOnCard());
        deck.setImagesRequired(expectedCardCount);

        return deckRepository.save(deck);
    }

    public Project updateDeckMeta(DeckDTO deckDto, long id) {
        User currentUser = userService.getCurrentUser();
        Optional<Project> deckOpt = deckRepository.findById(id);
        if (deckOpt.isPresent()) {
            Project deck = deckOpt.get();
            if (deck.getOwner().getId() != currentUser.getId()) {
                throw new ResourceNotFoundException();
            }
            deck.setName(deckDto.getName());
            deck.setDescription(deckDto.getDescription());
            deck.setOwner(currentUser);
            return deckRepository.save(deck);
        }

        throw new ResourceNotFoundException();
    }

    public Project setBackSideImageKey(String backSideImageKey, long id) {
        User currentUser = userService.getCurrentUser();
        Optional<Project> deckOpt = deckRepository.findById(id);
        if (deckOpt.isPresent()) {
            Project deck = deckOpt.get();
            if (deck.getOwner().getId() != currentUser.getId()) {
                throw new ResourceNotFoundException();
            }
            deck.setBacksideKey(backSideImageKey);
            return deckRepository.save(deck);
        }
        throw new ResourceNotFoundException();
    }

    public Set<Project> findAllOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        currentUser = userService.findUserByUsername(currentUser.getUsername());
        return currentUser.getDecks();
    }

    public List<Project> findAll() {
        return deckRepository.findAll();
    }

    public Project getByIdEnrichedWithCards(long id) {
        Optional<Project> byId = deckRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Project deck = byId.get();
        deck.setCards(cardsService.getDeckData(deck));
        return deck;
    }

    @Transactional
    public Project submitData(long id, List<List<CardImage>> cards) {
        Optional<Project> byId = deckRepository.findById(id);
        Project deck;
        if (byId.isPresent()) {
            deck = byId.get();
        } else {
            throw new ResourceNotFoundException();
        }
        List<CardImage> cardImages = cards.stream().flatMap(Collection::stream).collect(Collectors.toList());
        cardsService.persistCardImages(cardImages);
        return getByIdEnrichedWithCards(id);
    }

    public Project getById(long id) {
        Optional<Project> byId = deckRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return byId.get();
    }
}
