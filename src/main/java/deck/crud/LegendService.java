package deck.crud;

import deck.dto.LegendElementDto;
import deck.model.Deck;
import deck.model.Image;
import deck.model.LegendElement;
import deck.repository.LegendElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class LegendService {

    private final DeckService deckService;
    private final LegendElementRepository legendElementRepository;

    @Autowired
    public LegendService(DeckService deckService, LegendElementRepository legendElementRepository) {
        this.deckService = deckService;
        this.legendElementRepository = legendElementRepository;
    }

    public List<LegendElement> getLegend(long deckId) {
        List<LegendElement> allByDeckId = legendElementRepository.findAllByDeckId(deckId);
        if (allByDeckId.size() == 0) {
            Deck byId = deckService.getById(deckId);
            if (byId.getImagesRequired() == 0) {
                generateLegend(deckId);
                return getLegend(deckId);
            }
        }
        return allByDeckId;

    }

    @Transactional
    protected void generateLegend(long deckId) {
        Deck byId = deckService.getById(deckId);
        List<Image> images = byId.getImages();
        List<LegendElement> allByDeckId = new ArrayList<>();
        for (Image image : images) {
            LegendElement textElement = new LegendElement();
            textElement.setCardNumber(0);
            textElement.setContent(image.getText());
            textElement.setPositionX(1);
            textElement.setPositionY(1);
            textElement.setDeck(byId);
            textElement.setLegendSourceType(LegendElementDto.LegendSourceType.TEXT);
            allByDeckId.add(textElement);

            LegendElement imageElement = new LegendElement();
            imageElement.setCardNumber(0);
            imageElement.setContent(image.getUrl());
            imageElement.setPositionX(12);
            imageElement.setPositionY(12);
            imageElement.setDeck(byId);
            imageElement.setLegendSourceType(LegendElementDto.LegendSourceType.IMAGE);
            allByDeckId.add(imageElement);
        }
        legendElementRepository.saveAll(allByDeckId);
    }

}
