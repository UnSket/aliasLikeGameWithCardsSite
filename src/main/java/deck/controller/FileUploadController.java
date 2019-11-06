package deck.controller;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import deck.crud.DeckService;
import deck.crud.ImageService;
import deck.model.Deck;
import deck.model.ImageElement;
import deck.storage.ImageStorageFileNotFoundException;
import deck.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class FileUploadController {

    private final StorageService storageService;
    private final DeckService deckService;
    private final ImageService imageService;

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
    @Transactional
    public ResponseEntity handleFileUpload(@RequestParam("files") List<MultipartFile> files,
                                           @RequestParam("deckId") Long deckId,
                                           @RequestParam(required = false, value = "bgCleanUpFlag")
                                                       Boolean needBgCleanUp) {

        List<String> collect = new ArrayList<>();
        for (MultipartFile file : files) {
            String store = storageService.store(file, needBgCleanUp);
            collect.add(store);
        }
        List<ImageElement> images = new ArrayList<>();
        for (String z : collect) {
            ImageElement image = imageService.submitNewAndGet(z, deckId);
            images.add(image);
        }
        return ResponseEntity.ok(images);
    }

    @PostMapping("/api/files/single")
    @ResponseBody
    public ResponseEntity handleSingleFileUpload(@RequestParam("files") MultipartFile file,
                                                 @RequestParam("deckId") Long deckId,
                                                 @RequestParam(required = false, value = "bgCleanUpFlag") Boolean needBgCleanUp) {
        String key = storageService.store(file, needBgCleanUp);
        ImageElement image = imageService.submitNewAndGet(key, deckId);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/api/deck/backside/{id:.+}")
    @ResponseBody
    public ResponseEntity handleBackSideUpload(@RequestParam("files") MultipartFile file,
                                               @RequestParam("deckId") Long deckId,
                                               @RequestParam(required = false, value = "bgCleanUpFlag") Boolean needBgCleanUp) {
        Deck deck = deckService.getById(deckId);
        if (deck == null) {
            throw new ResourceNotFoundException();
        }
        String key = storageService.store(file, needBgCleanUp);
        imageService.submitNewAndGet(key);
        deck = deckService.setBackSideImageKey(key, deckId);
        return ResponseEntity.ok(deck);
    }

    @PostMapping("/api/files/change")
    public ResponseEntity<ImageElement> changeImage(@RequestParam("file") MultipartFile newImage,
                                             @RequestParam("imageId") Long imageId,
                                             @RequestParam(required = false, value = "bgCleanUpFlag") Boolean needBgCleanUp) {
        String newUrl = storageService.store(newImage, needBgCleanUp);
        return ResponseEntity.ok(imageService.updateImageAndGet(imageId, newUrl));
    }

    @PostMapping("/api/files/backimage/")
    @ResponseBody
    public ImageElement handleFileUpload(@RequestParam("file") MultipartFile file,
                                  @RequestParam(required = false, value = "bgCleanUpFlag") Boolean needBgCleanUp) {
        String key = storageService.store(file, needBgCleanUp);
        return imageService.submitNewAndGet(key);
    }

    @ExceptionHandler(ImageStorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ImageStorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
