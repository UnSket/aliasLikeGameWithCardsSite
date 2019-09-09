package deck.controller;

import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.dto.DeckDTO;
import deck.dto.ImageTextConfigDTO;
import deck.dto.ImageTextLegendDTO;
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

    // TODO pageable search by name description
    // LEGEND
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

    @PutMapping("api/deck/update")
    public ResponseEntity<Deck> updateDeck(@RequestBody DeckDTO deck) {
        Deck deckRes = deckService.updateDeckMeta(deck);
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("api/deck/text/config")
    public ResponseEntity<Deck> updateDeckTextSize(ImageTextConfigDTO imageTextConfigDTO) {
        Deck deckRes = deckService.updateDeckTextSize(imageTextConfigDTO.getId(), imageTextConfigDTO.getSize());
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("api/deck/cards")
    public Deck submitDeckContent(@RequestBody UpdateCardsDto cards) {
        return deckService.submitData(cards);
    }

    @PostMapping("api/deck/text/legend")
    public Image submitImageText(@RequestBody ImageTextLegendDTO imagetextLegendDTO) {
        //TODO: validate image correspond to deck
        return imageService.submitImageText(imagetextLegendDTO.getImageId(), imagetextLegendDTO.getText());
    }

}
