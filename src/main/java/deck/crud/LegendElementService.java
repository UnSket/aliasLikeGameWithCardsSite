package deck.crud;

import deck.dto.ImageTextLegendDTO;
import deck.dto.LegendDTO;
import deck.dto.LegendElementDto;
import deck.dto.UpdateLegendDto;
import deck.model.Deck;
import deck.model.ImageElement;
import deck.model.Legend;
import deck.model.LegendElement;
import deck.repository.DeckRepository;
import deck.repository.ImageRepository;
import deck.repository.LegendElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LegendElementService {

    private static final int OFFSET = 18;
    private static final int DIAMETER = 336;
    private static final float TEXT_SIZE_FACTOR = 3f;
    private final DeckService deckService;
    private final DeckRepository deckRepository;
    private final LegendElementRepository legendElementRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public LegendElementService(DeckService deckService,
                                LegendElementRepository legendElementRepository,
                                DeckRepository deckRepository,
                                ImageRepository imageRepository) {
        this.deckService = deckService;
        this.deckRepository = deckRepository;
        this.legendElementRepository = legendElementRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void setText(ImageTextLegendDTO imageTextLegendDTO, Legend legend) {
        Set<LegendElement> legendElements = legend.getItems();

        List<LegendElement> imageLegends = legendElements.stream()
                .filter(legendElement -> legendElement.getImageId().equals(imageTextLegendDTO.getImageId()) &&
                        legendElement.getLegendSourceType() == LegendElementDto.LegendSourceType.TEXT)
                .collect(Collectors.toList());
        if (imageLegends.size() > 1) {
            throw new RuntimeException("too many legends for one image");
        }

        if (imageLegends.size() == 1) {
            LegendElement legendElement = imageLegends.get(0);
            legendElement.setSource(imageTextLegendDTO.getText());
            legendElementRepository.save(legendElement);
            return;
        }

        LegendElement textElement = new LegendElement();
        textElement.setSource(imageTextLegendDTO.getText());
        textElement.setLegendSourceType(LegendElementDto.LegendSourceType.TEXT);
        textElement.setImageId(imageTextLegendDTO.getImageId());
        textElement.setLegend(legend);

        ImageElement image = imageRepository.getOne(imageTextLegendDTO.getImageId());
        LegendElement imageElement = new LegendElement();
        imageElement.setSource(image.getUrl());
        imageElement.setLegendSourceType(LegendElementDto.LegendSourceType.IMAGE);
        imageElement.setImageId(image.getId());
        imageElement.setLegend(legend);

        legendElementRepository.save(imageElement);
        legendElementRepository.save(textElement);

        legendElements.add(imageElement);
        legendElements.add(textElement);
    };

    @Transactional
    public List<LegendElement> updateLegendElements(Legend legend, UpdateLegendDto legendElementDtos) {
        Set<LegendElement> allByDeckId = legend.getItems();
        List<LegendElement> collect = Stream.of(legendElementDtos.getCards()).map(dto -> {
            LegendElement element = allByDeckId.stream().filter(z -> z.getId() == dto.getId()).findFirst().get();
            element.setPositionX(dto.getPositionX());
            element.setPositionY(dto.getPositionY());
            element.setCardNumber(dto.getCardNumber());
            element.setId(dto.getId());
            return element;
        }).collect(Collectors.toList());
        legendElementRepository.saveAll(collect);
        return collect;
    }

    public List<LegendElement> getLegend(long legendId) {
        List<LegendElement> allByLegendId = legendElementRepository.findAllByLegendId(legendId);
        return allByLegendId;
    }

    private float getCardLimitOnLine(int currentLineY, int textSize){
        return (float)(
                DIAMETER * Math.sin(
                (Math.PI/2.0f-Math.abs(Math.asin(2.0f*currentLineY/DIAMETER)))
                )
                /
                        (OFFSET + TEXT_SIZE_FACTOR * textSize) + 2.1f);
    }

    /*@Transactional
    protected void generateLegend(long deckId) {
        Deck byId = deckService.getById(deckId);
        List<ImageElement> images = byId.getImages();
        List<LegendElement> allByDeckId = new ArrayList<>();
        int imagesNumber = byId.getImages().size();
        int textSize = byId.getTextSize();

        int currentImageNumber = 0;
        int currentCardNumber = 0;

        int currentLineY = DIAMETER/2-2*OFFSET;
        float currentLineXLimit = getCardLimitOnLine(currentLineY, textSize);
        int currentLineX = -(int)(Math.sqrt(DIAMETER*DIAMETER/4-currentLineY*currentLineY))+OFFSET/2;

        while (currentImageNumber<imagesNumber){
           if(currentLineXLimit>0){
               if(currentLineY*currentLineY+
                       currentLineX*currentLineX>
                       DIAMETER*DIAMETER/4){
                   currentLineX+=(int)(OFFSET + TEXT_SIZE_FACTOR * textSize)/4;
                   currentLineXLimit-=0.12;
                    continue;
               }
               if((currentLineY-textSize*TEXT_SIZE_FACTOR - textSize - OFFSET*1.25f)*
                       (currentLineY-textSize*TEXT_SIZE_FACTOR - textSize - OFFSET*1.25f)+
                       currentLineX*currentLineX>
                       DIAMETER*DIAMETER/4){
                   currentLineX+=(int)(OFFSET + TEXT_SIZE_FACTOR * textSize)/4;
                   currentLineXLimit-=0.12;
                   continue;
               }
               if(currentLineY*currentLineY+
                       (currentLineX + OFFSET + TEXT_SIZE_FACTOR * textSize)*
                               (currentLineX + OFFSET + TEXT_SIZE_FACTOR * textSize)>
                       DIAMETER*DIAMETER/4){
                   currentLineX+=(int)(OFFSET + TEXT_SIZE_FACTOR * textSize)/4;
                   currentLineXLimit-=0.12;
                   continue;
               }
               if((currentLineY-textSize*TEXT_SIZE_FACTOR - textSize - OFFSET*1.25f)*
                       (currentLineY-textSize*TEXT_SIZE_FACTOR - textSize - OFFSET*1.25f)+
                        (currentLineX + OFFSET + TEXT_SIZE_FACTOR * textSize)*
                       (currentLineX + OFFSET + TEXT_SIZE_FACTOR * textSize)>
                       DIAMETER*DIAMETER/4){
                   currentLineX+=(int)(OFFSET + TEXT_SIZE_FACTOR * textSize)/4;
                   currentLineXLimit-=0.12;
                   continue;
               }
               allocateElement(images.get(currentImageNumber++),
                       allByDeckId,
                       currentCardNumber,
                       currentLineX,
                       currentLineY,
                       textSize);
               currentLineX+=(int)(OFFSET + TEXT_SIZE_FACTOR * textSize);
           }else{
               currentLineY -= (int)(OFFSET*1.25f + TEXT_SIZE_FACTOR*textSize + textSize);
               currentLineXLimit = getCardLimitOnLine(currentLineY, textSize);
               if(currentLineY<-DIAMETER/2){
                   currentCardNumber++;
                   currentLineY = DIAMETER/2-2*OFFSET;
                   currentLineXLimit = getCardLimitOnLine(currentLineY, textSize);
               }
               currentLineX = -(int)(Math.sqrt(((DIAMETER * DIAMETER) / 4) - (currentLineY * currentLineY)))+OFFSET/2;
           }
        }

        legendElementRepository.saveAll(allByDeckId);
    }
    private void allocateElement(ImageElement image, List<LegendElement> allByDeckId,
                                 int cardNumber, int x, int y, int textSize){

        LegendElement imageElement = new LegendElement();
        imageElement.setCardNumber(cardNumber);
        imageElement.setContent(image.getUrl());
        imageElement.setImageId(image.getId());
        //imageElement.setDeck(image.getDeck());
        imageElement.setPositionX(x+DIAMETER/2);
        imageElement.setPositionY(-y+DIAMETER/2);
        imageElement.setLegendSourceType(LegendElementDto.LegendSourceType.IMAGE);
        allByDeckId.add(imageElement);

        LegendElement textElement = new LegendElement();
        textElement.setCardNumber(cardNumber);
        textElement.setContent(image.getText());
        textElement.setImageId(image.getId());
        textElement.setPositionX(x+DIAMETER/2);
        textElement.setPositionY((int)(-y+textSize*TEXT_SIZE_FACTOR+OFFSET*0.25f)+DIAMETER/2);
        //textElement.setDeck(image.getDeck());
        textElement.setLegendSourceType(LegendElementDto.LegendSourceType.TEXT);
        allByDeckId.add(textElement);
    }*/

}
