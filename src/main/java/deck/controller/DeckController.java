package deck.controller;

import deck.crud.DeckService;
import deck.dto.CardImageDto;
import deck.dto.CreateDeckDTO;
import deck.model.Deck;
import deck.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class DeckController {

    private DeckService deckService;

    private StorageService storageService;

    @Autowired
    public DeckController(DeckService deckService, StorageService storageService) {
        this.deckService = deckService;
        this.storageService = storageService;
    }

    @GetMapping("/api/deck/")
    public List<Deck> listDecks() {
        return deckService.findAll();
    }

    @PostMapping("/api/deck/")
    public ResponseEntity<Long> submitDeck(@RequestBody CreateDeckDTO deck) {
        long id = deckService.submitNewAndGetId(deck);
        return ResponseEntity.ok(id);
    }

    //TODO: swagger shitting
    @PostMapping("/api/deck/{id:.+}")
    public ResponseEntity<Resource> submitDeckContent(@RequestParam("id") long id,
                                                      @RequestBody List<List<List<CardImageDto>>> deckContent) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/deck/{id:.+}")
    public ResponseEntity<Resource> uploadBackImage(@RequestParam("id") long id, @RequestParam("file") MultipartFile file) {
        storageService.store(file);
        return ResponseEntity.ok().build();
    }


}
