package deck.controller;

import deck.crud.DeckService;
import deck.dto.CardImageDto;
import deck.dto.DeckDTO;
import deck.model.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
        Deck deck = deckService.getById(id);
        System.out.println(deck.getImages());
        return deck;
    }

    @PostMapping("api/deck")
    public ResponseEntity<Deck> submitDeck(@RequestBody DeckDTO deck) {
        Deck deckRes = deckService.submitNewDeck(deck);
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("api/deck/backside/{id:.+}/key/{backsideImageKey:.+}")
    public ResponseEntity<Deck> submitDeckBackSideImage(@RequestParam("id") long id,
                                                        @RequestParam("backsideImageKey") String backsideImageKey) {
        Deck deckRes = deckService.setBackSideImageKey(backsideImageKey, id);
        return ResponseEntity.ok(deckRes);
    }

    @PutMapping("api/deck/{id:.+}")
    public ResponseEntity<Deck> updateDeck(@RequestParam("id") long id,
                                           @RequestBody DeckDTO deck) {
        Deck deckRes = deckService.updateDeck(deck, id);
        return ResponseEntity.ok(deckRes);
    }

    //TODO: swagger shitting
    @PostMapping("api/deck/{id:.+}")
    public ResponseEntity<Resource> submitDeckContent(@RequestParam("id") long id,
                                                      @RequestBody List<List<List<CardImageDto>>> deckContent) {
        return ResponseEntity.ok().build();
    }

}
