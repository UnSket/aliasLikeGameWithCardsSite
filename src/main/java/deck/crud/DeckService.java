package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.dto.DeckDTO;
import deck.model.Deck;
import deck.model.User;
import deck.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeckService {

    private final CardsService cardsService;
    private final DeckRepository deckRepository;
    private final UserService userService;

    @Autowired
    public DeckService(CardsService cardsService, DeckRepository deckRepository, UserService userService) {
        this.cardsService = cardsService;
        this.deckRepository = deckRepository;
        this.userService = userService;
    }

    public Deck submitNewDeck(DeckDTO deckDto) {
        User currentUser = userService.getCurrentUser();
        Deck deck = new Deck();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        deck.setImagesOnCard(deckDto.getImagesOnCard());
        deck.setOwner(currentUser);
        return deckRepository.save(deck);
    }

    public Deck updateDeck(DeckDTO deckDto, long id) {
        User currentUser = userService.getCurrentUser();
        Optional<Deck> deckOpt = deckRepository.findById(id);
        if (deckOpt.isPresent()) {
            Deck deck = deckOpt.get();
            if (deck.getOwner().getId() != currentUser.getId()) {
                throw new ResourceNotFoundException();
            }
            deck.setName(deckDto.getName());
            deck.setDescription(deckDto.getDescription());
            deck.setImagesOnCard(deckDto.getImagesOnCard());
            deck.setOwner(currentUser);
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

    public Set<Deck> findAllOfCurrentUser() {
        User currentUser = userService.getCurrentUser();
        currentUser = userService.findUserByUsername(currentUser.getUsername());
        return currentUser.getDecks();
    }

    public List<Deck> findAll() {
        return deckRepository.findAll();
    }

    public Deck getById(long id) {
        Optional<Deck> byId = deckRepository.findById(id);
        if (!byId.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Deck deck = byId.get();
        deck.setCards(cardsService.getDeckData(deck));
        return deck;
    }
}
