package deck.crud;

import deck.dto.CreateDeckDTO;
import deck.model.Deck;
import deck.repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {


    private final DeckRepository deckRepository;

    @Autowired
    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
    }

    public long submitNewAndGetId(CreateDeckDTO deckDto){
        Deck deck = new Deck();
        deck.setName(deckDto.getName());
        deck.setDescription(deckDto.getDescription());
        deck.setImagesOnCard(deckDto.getImagesOnCard());
        Deck save = deckRepository.save(deck);
        return save.getId();
    }

    public List<Deck> findAll(){
        return deckRepository.findAll();
    }
}
