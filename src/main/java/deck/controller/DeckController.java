package deck.controller;

import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.dto.CardImageDto;
import deck.dto.CreateDeckDTO;
import deck.dto.EditDeckDTO;
import deck.model.Deck;
import deck.model.Image;
import deck.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class DeckController {

    private final DeckService deckService;
    private final StorageService storageService;
    private final ImageService imageService;

    @Autowired
    public DeckController(DeckService deckService, StorageService storageService, ImageService imageService) {
        this.deckService = deckService;
        this.storageService = storageService;
        this.imageService = imageService;
    }

    @GetMapping("api/decks")
    public Set<Deck> listDecks() {
        return deckService.findAllOfCurrentUser();
    }

    @GetMapping("api/deck/{id}")
    public Deck getDeck(@PathVariable(value="id") Long id) {
        Deck deck = deckService.getById(id);
        System.out.println(deck.getImages());
        return deck;
    }

    @PostMapping("api/deck")
    public ResponseEntity<Deck> submitDeck(@RequestBody CreateDeckDTO deck) {
        Deck deckRes = deckService.submitNewAndGetId(deck);
        return ResponseEntity.ok(deckRes);
    }

    @PostMapping("api/deck/edit")
    public ResponseEntity<Deck> editDeck(@RequestBody EditDeckDTO deck) {
        Deck deckRes = deckService.editDeck(deck);
        return ResponseEntity.ok(deckRes);
    }

    //TODO: swagger shitting
    @PostMapping("api/deck/{id:.+}")
    public ResponseEntity<Resource> submitDeckContent(@RequestParam("id") long id,
                                                      @RequestBody List<List<List<CardImageDto>>> deckContent) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/deck/{id:.+}")
    public ResponseEntity<Resource> uploadBackImage(@RequestParam("id") long id, @RequestParam("file") MultipartFile file) {
        storageService.store(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/deck/backside")
    @ResponseBody
    public ResponseEntity<Deck> handleFileUpload(@RequestParam("file") MultipartFile file,
                                           @RequestParam("deckId") Long deckId) {
        String key = storageService.store(file);
        Image image = imageService.submitNewAndGet(key);
        Deck deck = deckService.changeBackside(image, deckId);
        return ResponseEntity.ok(deck);
    }
}
