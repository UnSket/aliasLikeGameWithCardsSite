package deck.controller;

import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.dto.*;
import deck.model.CardImage;
import deck.model.Deck;
import deck.model.ImageElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
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

    // LEGEND
    @GetMapping("/api/decks/my")
    public Set<Deck> listDecks() {
        return deckService.findAllOfCurrentUser();
    }

    @GetMapping("/api/deck/{id}")
    public Deck getDeck(@PathVariable(value = "id") Long id) {
        return deckService.getById(id);
    }

    @GetMapping("/api/deck/enriched/{id}")
    public List<List<CardImage>> getByIdEnrichedWithCards(@PathVariable(value = "id") Long id) {
        return deckService.getByIdEnrichedWithCards(id);
    }

    @PostMapping("/api/deck")
    public ResponseEntity<Deck> submitDeck(@RequestBody DeckDTO deck) {
        Deck deckRes = deckService.submitNewDeck(deck);
        return ResponseEntity.ok(deckRes);
    }

    @PutMapping("/api/deck/update")
    public ResponseEntity<Deck> updateDeck(@RequestBody DeckDTO deck) {
        Deck deckRes = deckService.updateDeckMeta(deck);
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("/api/deck/cards")
    public List<List<CardImage>>  submitDeckContent(@RequestBody UpdateCardsDto cards) {
        return deckService.submitData(cards);
    }

    @PostMapping("/api/deck/text/legend")
    public ImageElement submitImageText(@RequestBody ImageTextLegendDTO imagetextLegendDTO) {
        //TODO: validate image correspond to deck
        return imageService.submitImageText(imagetextLegendDTO.getImageId(), imagetextLegendDTO.getText());
    }

    @PostMapping(value = "/api/decks")
    public Page<Deck> getDecks(@NotNull final Pageable pageable, @RequestBody DeckFilter filter) {
        return deckService.find(pageable, filter);
    }

}
