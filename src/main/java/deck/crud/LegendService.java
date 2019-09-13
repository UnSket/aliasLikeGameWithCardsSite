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

    private static final int OFFSET = 20;
    private static final int DIAMETER = 336;
    private static final float TEXT_SIZE_FACTOR = 3f;
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
        Stream.of(legendElementDtos.getCards()).forEach(dto ->{
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

    private int getCardLimitOnLine(int currentLineY, int textSize){
        return (int) (
                DIAMETER * Math.sin(
                (Math.PI/2-Math.abs(Math.asin(2*currentLineY/DIAMETER)))
                /2
                )
                /
                        (OFFSET + TEXT_SIZE_FACTOR * textSize)
        );
    }
    @Transactional
    protected void generateLegend(long deckId) {
        Deck byId = deckService.getById(deckId);
        List<Image> images = byId.getImages();
        List<LegendElement> allByDeckId = new ArrayList<>();
        int imagesNumber = byId.getImages().size();
        int textSize = byId.getTextSize();

        int currentImageNumber = 0;
        int currentCardNumber = 0;

        int currentLineY = -DIAMETER/2+OFFSET;
        int currentLineXLimit = getCardLimitOnLine(currentLineY, textSize);
        int currentLineX = -(int)(Math.sqrt(DIAMETER*DIAMETER/4-currentLineY*currentLineY));

        while (currentImageNumber<imagesNumber){
           if(currentLineXLimit>0){
               allocateElement(images.get(currentCardNumber),
                       allByDeckId,
                       currentCardNumber,
                       currentLineX,
                       currentLineY,
                       textSize);
               currentImageNumber++;
               currentLineXLimit--;
               currentLineX+=(int)(OFFSET + TEXT_SIZE_FACTOR * textSize);
           }else{
               currentLineY += (int)(currentLineY + OFFSET*2 + TEXT_SIZE_FACTOR*textSize*2);
               currentLineXLimit = getCardLimitOnLine(currentLineY, textSize);
               if(currentLineY<-DIAMETER/2){
                   currentCardNumber++;
                   currentLineY = DIAMETER/2-OFFSET;
                   currentLineXLimit = getCardLimitOnLine(currentLineY, textSize);
               }
               currentLineX = -(int)(Math.sqrt(((DIAMETER * DIAMETER) / 4) - (currentLineY * currentLineY)));
           }
        }

        legendElementRepository.saveAll(allByDeckId);
    }
    private void allocateElement(Image image, List<LegendElement> allByDeckId,
                                 int cardNumber, int x, int y, int textSize){
        LegendElement textElement = new LegendElement();
        textElement.setCardNumber(cardNumber);
        textElement.setContent(image.getText());
        textElement.setPositionX(x+DIAMETER/2);
        textElement.setPositionY(y+DIAMETER/2);
        textElement.setDeck(image.getDeck());
        textElement.setLegendSourceType(LegendElementDto.LegendSourceType.TEXT);
        allByDeckId.add(textElement);

        LegendElement imageElement = new LegendElement();
        imageElement.setCardNumber(cardNumber);
        imageElement.setContent(image.getUrl());
        imageElement.setPositionX(x+DIAMETER/2);
        imageElement.setPositionY((int)(y+textSize*TEXT_SIZE_FACTOR+OFFSET)+DIAMETER/2);
        imageElement.setDeck(image.getDeck());
        imageElement.setLegendSourceType(LegendElementDto.LegendSourceType.IMAGE);
        allByDeckId.add(imageElement);
    }

}
