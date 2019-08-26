package deck.controller;

import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.model.Deck;
import deck.model.Image;
import deck.storage.ImageStorageFileNotFoundException;
import deck.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileUploadController {

    private final StorageService storageService;
    private final DeckService deckService;
    private final ImageService imageService;

    //TODO по тексту генерится картинка 200*200 центрированная 12 - 150 (переносы по словам)
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
        List<Image> images = files.stream().map(file -> {
            String key = storageService.store(file, true);
            return imageService.submitNewAndGet(key, deck);
        }).collect(Collectors.toList());
        return ResponseEntity.ok(images);
    }

    @PostMapping("/api/files/single")
    @ResponseBody
    public ResponseEntity handleSingleFileUpload(@RequestParam("files") MultipartFile file,
                                                 @RequestParam("deckId") Long deckId) {
        Deck deck = deckService.getById(deckId);
        String key = storageService.store(file, true);
        Image image = imageService.submitNewAndGet(key, deck);
        return ResponseEntity.ok(image);
    }

    //TODO: check this method;
    @PostMapping("api/files/change")
    public ResponseEntity<Image> changeImage(@RequestParam("file") MultipartFile newImage,
                                             @RequestParam("imageId") Long imageId) {
        String newUrl = storageService.store(newImage, true);
        return ResponseEntity.ok(imageService.updateImageAndGet(imageId, newUrl));
    }

    @PostMapping("/api/files/backimage/")
    @ResponseBody
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        String key = storageService.store(file, false);
        //String key = storageService.store(file, false);
        return ResponseEntity.ok(imageService.submitNewAndGet(key));
    }

    @ExceptionHandler(ImageStorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ImageStorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
