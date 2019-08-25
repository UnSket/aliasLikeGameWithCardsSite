package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.dto.CreateDeckDTO;
import deck.dto.EditDeckDTO;
import deck.model.Deck;
import deck.model.Image;
import deck.model.User;
import deck.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeckService {


    private final DeckRepository deckRepository;
    private final UserService userService;

    @Autowired
    public DeckService(DeckRepository deckRepository, UserService userService) {
        this.deckRepository = deckRepository;
        this.userService = userService;
    }

    public Deck submitNewAndGetId(CreateDeckDTO deckDto){
        User currentUser = userService.getCurrentUser();
        Deck deck = new Deck();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        deck.setImagesOnCard(deckDto.getImagesOnCard());
        deck.setOwner(currentUser);
        return deckRepository.save(deck);
    }

    public Deck editDeck(EditDeckDTO deckDto){
        Deck deck = deckRepository.findById(deckDto.getId()).orElseThrow(() -> new RuntimeException("deck with id " + deckDto.getId() + " not found"));
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        return deckRepository.save(deck)    ;
    }

    public Set<Deck> findAllOfCurrentUser(){
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
        return byId.orElseGet(Deck::new);
    }

    public Deck changeBackside(Image image, Long deckId) {
        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new RuntimeException("deck with id " + deckId + " not found"));
        deck.setBackside(image);
        return deckRepository.save(deck);
    }
}
