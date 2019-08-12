package deck.controller;

import com.google.common.collect.Lists;
import deck.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//TODO: push to github и чтобы собиралось все без всякого  из коробочки
//TODO: improve authorization controller

@RestController
public class CardController {

    @Autowired
    public CardController() {
    }

    @GetMapping("/card/")
    public List<Card> listCards() {
        return Lists.newArrayList();
    }

    @PostMapping("/card/")
    public ResponseEntity<Resource> submitCard(@RequestBody Card deck) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/card/{id:.+}")
    public ResponseEntity<Resource> handleFileUpload(@RequestParam("id") long id) {
        return ResponseEntity.ok().build();
    }

}
