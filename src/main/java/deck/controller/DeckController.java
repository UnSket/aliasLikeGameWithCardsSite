package deck.controller;

import deck.crud.DeckService;
import deck.dto.DeckDTO;
import deck.model.CardImage;
import deck.model.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class DeckController {

    private final DeckService deckService;

    @Autowired
    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("api/decks")
    public Set<Deck> listDecks() {
        return deckService.findAllOfCurrentUser();
    }

    @GetMapping("api/deck/{id}")
    public Deck getDeck(@PathVariable(value = "id") Long id) {
        return deckService.getById(id);
    }

    @GetMapping("api/deck/enriched/{id}")
    public Deck getByIdEnrichedWithCards(@PathVariable(value = "id") Long id) {
        return deckService.getByIdEnrichedWithCards(id);
    }

    @PostMapping("api/deck")
    public ResponseEntity<Deck> submitDeck(@RequestBody DeckDTO deck) {
        Deck deckRes = deckService.submitNewDeck(deck);
        return ResponseEntity.ok(deckRes);
    }

    @PutMapping("api/deck/{id:.+}")
    public ResponseEntity<Deck> updateDeck(@RequestParam("id") long id,
                                           @RequestBody DeckDTO deck) {
        Deck deckRes = deckService.updateDeckMeta(deck, id);
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("api/deck/{id:.+}")
    public Deck submitDeckContent(@RequestParam("id") long id,
                                  @RequestBody List<List<CardImage>> cards) {
        return deckService.submitData(id, cards);
    }

}
