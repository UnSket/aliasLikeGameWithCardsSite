package deck.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.model.Deck;
import deck.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import deck.storage.ImageStorageFileNotFoundException;
import deck.storage.StorageService;

@RestController
public class FileUploadController {

    private final StorageService storageService;
    private final DeckService deckService;
    private final ImageService imageService;
    //TODO: add image binding to deck on creation/upload

    //ADD method that binds image id, text (name of subject), font size, color;
    // по тексту генерится картинка 200*200 центрированная 12 - 150 (переносы по словам)

    //TODO: подумать над списком файлы грузить
    @Autowired
    public FileUploadController(StorageService storageService, DeckService deckService, ImageService imageService) {
        this.storageService = storageService;
        this.deckService = deckService;
        this.imageService = imageService;
    }

    @GetMapping("/api/files/list")
    public List<String> listUploadedFiles() throws IOException {
        return storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList());
    }

    //TODO: add image cut off (336 px)
    //bg cut (test)
    @GetMapping("/api/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/api/files")
    @ResponseBody
    public ResponseEntity handleFileUpload(@RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("deckId") Long deckId) {
        Deck deck = deckService.getById(deckId);
        List<String> keys = files.stream().map(file -> {
            String key = storageService.store(file);
            imageService.submitNewAndGetId(key, deck);
            return key;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(keys);
    }

    @PostMapping("/api/files/single/")
    @ResponseBody
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        String key = storageService.store(file);
        return ResponseEntity.ok(key);
    }

    @ExceptionHandler(ImageStorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ImageStorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
