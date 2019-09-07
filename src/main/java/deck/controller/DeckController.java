package deck.controller;

import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.dto.DeckDTO;
import deck.dto.UpdateCardsDto;
import deck.model.Deck;
import deck.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class DeckController {

    private final DeckService deckService;
    private final ImageService imageService;

    @Autowired
    public DeckController(DeckService deckService, ImageService imageService) {
        this.deckService = deckService;
        this.imageService = imageService;
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

    @PostMapping("api/deck/{id:.+}/text/{size:.+}")
    public ResponseEntity<Deck> updateDeckTextSize(@RequestParam("id") long id, @RequestParam("size") int size) {
        Deck deckRes = deckService.updateDeckTextSize(id, size);
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("api/deck/{id:.+}")
    public Deck submitDeckContent(@RequestParam("id") long id,
                                  @RequestBody UpdateCardsDto cards) {
        return deckService.submitData(cards);
    }

    @PostMapping("api/deck/{id:.+}/image/{imageId:.+}/text/{text:.+}")
    public Image submitImageText(@RequestParam("id") long id,
                                 @RequestParam("imageId")  long imageId,
                                 @RequestParam("text") String text) {
        return imageService.submitImageText(imageId, text);
    }

}
