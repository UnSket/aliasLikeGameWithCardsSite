package deck.crud;

import deck.controller.ResourceNotFoundException;
import deck.dto.CreateDeckDTO;
import deck.model.Deck;
import deck.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeckService {


    private final DeckRepository deckRepository;

    @Autowired
    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public Deck submitNewAndGetId(CreateDeckDTO deckDto){
        Deck deck = new Deck();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        deck.setImagesOnCard(deckDto.getImagesOnCard());
        return deckRepository.save(deck);
    }

    public List<Deck> findAll(){
        return deckRepository.findAll();
    }

    public Deck getById(long id){
        Optional<Deck> byId = deckRepository.findById(id);
        if(!byId.isPresent()){
            throw new ResourceNotFoundException();
        }
        return byId.orElseGet(Deck::new);
    }
}
