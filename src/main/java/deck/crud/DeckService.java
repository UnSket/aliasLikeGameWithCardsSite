package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.dto.DeckDTO;
import deck.dto.DeckFilter;
import deck.dto.UpdateCardsDto;
import deck.image.generation.CardConfigurationProcessor;
import deck.model.CardImage;
import deck.model.Deck;
import deck.model.User;
import deck.repository.DeckRepository;
import deck.repository.LegendElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DeckService {

    private final CardsService cardsService;
    private final DeckRepository deckRepository;
    private final LegendElementRepository legendElementRepository;
    private final UserService userService;
    private final CardConfigurationProcessor cardConfigurationProcessor;

    // TODO create mock data for dev
    @Autowired
    public DeckService(CardsService cardsService,
                       DeckRepository deckRepository,
                       UserService userService,
                       CardConfigurationProcessor cardConfigurationProcessor,
                       LegendElementRepository legendElementRepository) {
        this.cardsService = cardsService;
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.cardConfigurationProcessor = cardConfigurationProcessor;
        this.legendElementRepository = legendElementRepository;
    }

    public Deck submitNewDeck(DeckDTO deckDto) {
        User currentUser = userService.getCurrentUser();
        Deck deck = new Deck();
        deck.setLegendTuned(false);
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        deck.setImagesOnCard(deckDto.getImagesOnCard());
        deck.setImages(new ArrayList<deck.model.Image>());
        deck.setOwner(currentUser);
        deck.setTextSize(15);

        int expectedCardCount = cardConfigurationProcessor.getExpectedImagesCountByImagesOnCard(deck.getImagesOnCard());
        deck.setImagesRequired(expectedCardCount);

        return deckRepository.save(deck);
    }

    public Deck updateDeckMeta(DeckDTO deckDto) {
        User currentUser = userService.getCurrentUser();
        if (deckDto.getId() == null) {
            throw new ResourceNotFoundException();
        }
        Optional<Deck> deckOpt = deckRepository.findById(deckDto.getId());
        if (deckOpt.isPresent()) {
            Deck deck = deckOpt.get();
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Deck updateDeckTextSize(long id, int size) {
        User currentUser = userService.getCurrentUser();
        Optional<Deck> deckOpt = deckRepository.findById(id);
        if (deckOpt.isPresent()) {
            // legendElementRepository.deleteAllByDeckId(id);
            Deck deck = deckOpt.get();
            if (deck.getOwner().getId() != currentUser.getId()) {
                throw new ResourceNotFoundException();
            }
            deck.setTextSize(size);
            return deckRepository.save(deck);
        }

        throw new ResourceNotFoundException();
    }

    public Deck setBackSideImageKey(String backSideImageKey, long id) {
        User currentUser = userService.getCurrentUser();
        Optional<Deck> deckOpt = deckRepository.findById(id);
        if (deckOpt.isPresent()) {
            Deck deck = deckOpt.get();
            if (deck.getOwner().getId() != currentUser.getId()) {
                throw new ResourceNotFoundException();
            }
            deck.setBacksideKey(backSideImageKey);
            return deckRepository.save(deck);
        }
        throw new ResourceNotFoundException();
    }

    public Page<Deck> find(Pageable pageable, DeckFilter deckFilter) {
        return deckRepository.findAll(deckFilter.toSpecification(userService.getCurrentUser()), pageable);
    }

    public Set<Deck> findAllOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        currentUser = userService.findUserByUsername(currentUser.getUsername());
        return currentUser.getDecks();
    }

    public List<Deck> findAll() {
        return deckRepository.findAll();
    }

    public List<List<CardImage>> getByIdEnrichedWithCards(long id) {
        Optional<Deck> byId = deckRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Deck deck = byId.get();
        return cardsService.getDeckData(deck);
    }

    @Transactional
    public List<List<CardImage>> submitData(UpdateCardsDto cards) {
        Optional<Deck> byId = deckRepository.findById(cards.getDeckId());
        Deck deck;
        if (byId.isPresent()) {
            deck = byId.get();
        } else {
            throw new ResourceNotFoundException();
        }
        //TODO: deck to cards bind validation
        List<CardImage> cardImages = Stream.of(cards.getCards()).flatMap(Stream::of).collect(Collectors.toList());
        cardsService.persistCardImages(cardImages);
        return getByIdEnrichedWithCards(cards.getDeckId());
    }

    public Deck getById(long id) {
        Optional<Deck> byId = deckRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return byId.get();
    }

    public User enrichUserWithDeckCount(User user) {
        Integer deckCount = deckRepository.countAllByOwner(user);
        if(deckCount == null ){
            deckCount = 0;
        }
        user.setDeckCount(deckCount);
        return user;
    }

    public Page<User> enrichUsersWithDeckCount(Page<User> users) {
        users.forEach(this::enrichUserWithDeckCount);
        return users;
    }
}
