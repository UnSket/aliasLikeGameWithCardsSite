package deck.crud;

import deck.dto.LegendElementDto;
import deck.dto.UpdateLegendDto;
import deck.model.Deck;
import deck.model.Image;
import deck.model.LegendElement;
import deck.repository.LegendElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LegendService {

    private final DeckService deckService;
    private final LegendElementRepository legendElementRepository;

    @Autowired
    public LegendService(DeckService deckService, LegendElementRepository legendElementRepository) {
        this.deckService = deckService;
        this.legendElementRepository = legendElementRepository;
    }

    public List<LegendElement> setLegend(UpdateLegendDto legendElementDtos) {
        long deckId = legendElementDtos.getDeckId();
        List<LegendElement> allByDeckId = legendElementRepository.findAllByDeckId(deckId);
        Stream.of(legendElementDtos.getLegendElementDtos()).forEach(dto ->{
            LegendElement element = allByDeckId.stream().filter(z -> z.getId() == dto.getId()).findFirst().get();
            element.setPositionX(element.getPositionX());
            element.setPositionY(element.getPositionY());
            element.setCardNumber(element.getCardNumber());
        });

        return legendElementRepository.saveAll(allByDeckId);
    }

    public List<LegendElement> getLegend(long deckId) {
        List<LegendElement> allByDeckId = legendElementRepository.findAllByDeckId(deckId);
        if (allByDeckId.size() == 0) {
            Deck byId = deckService.getById(deckId);
            if (byId.getImagesRequired() == 0) {
                generateLegend(deckId);
                return legendElementRepository.findAllByDeckId(deckId);
            }
        }
        return allByDeckId;
    }

    @Transactional
    protected void generateLegend(long deckId) {
        Deck byId = deckService.getById(deckId);
        List<Image> images = byId.getImages();
        List<LegendElement> allByDeckId = new ArrayList<>();
        int cardNumber = 0;
        int cardRadius = 336;
        for (Image image : images) {
            LegendElement textElement = new LegendElement();
            textElement.setCardNumber(0);
            textElement.setContent(image.getText());
            textElement.setPositionX(0);
            textElement.setPositionY(0);
            textElement.setDeck(byId);
            textElement.setLegendSourceType(LegendElementDto.LegendSourceType.TEXT);
            allByDeckId.add(textElement);

            LegendElement imageElement = new LegendElement();
            imageElement.setCardNumber(0);
            imageElement.setContent(image.getUrl());
            imageElement.setPositionX(0);
            imageElement.setPositionY(100);
            imageElement.setDeck(byId);
            imageElement.setLegendSourceType(LegendElementDto.LegendSourceType.IMAGE);
            allByDeckId.add(imageElement);
        }
        legendElementRepository.saveAll(allByDeckId);
    }

}
