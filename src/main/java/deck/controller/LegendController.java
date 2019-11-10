package deck.controller;

import deck.crud.DeckService;
import deck.crud.LegendElementService;
import deck.crud.LegendService;
import deck.dto.*;
import deck.model.Deck;
import deck.model.ImageElement;
import deck.model.Legend;
import deck.model.LegendElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LegendController {

    private final LegendElementService legendElementService;
    private final DeckService deckService;
    private final LegendService legendService;

    @Autowired
    public LegendController(LegendElementService legendElementService,
                            DeckService deckService,
                            LegendService legendService) {
        this.legendElementService = legendElementService;
        this.deckService = deckService;
        this.legendService = legendService;
    }

    @Transactional
    @GetMapping(value = "/api/legend/{id}")
    public LegendDTO getLegend(@PathVariable(value = "id") Long id) {
        Legend legend = legendService.getLegendById(id);
        List<LegendElement> legendSource = legendElementService.getLegend(id);
        return fromLegend(legend, legendSource);
    }

    @PostMapping(value = "/api/legend/update")
    public Legend updateLegend(@RequestBody UpdateLegendDto updateLegendDto) {
        Legend legend = legendService.updateLegend(updateLegendDto);
        return legend;
    }

    @PutMapping(value = "/api/legend/create")
    public Legend createLegend(@RequestBody CreateLegendDTO legendDTO) {
        Deck deck = deckService.getById(legendDTO.getDeckId());
        Legend legend = legendService.createLegend(deck, legendDTO);

        return legend;
    }

    @GetMapping(value = "/api/legends/{deckId}")
    public List<Legend> getDeckLegends(@PathVariable Long deckId) {
        List<Legend> legends = legendService.getDeckLegends(deckId);
        return legends;
    }

    @PostMapping("/api/legend/text")
    public Legend submitImageText(@RequestBody ImageTextLegendDTO imagetextLegendDTO) {
        Legend legend = legendService.getLegendById(imagetextLegendDTO.getLegendId());
        legendElementService.setText(imagetextLegendDTO, legend);
        return legend;
    }

    private LegendDTO fromLegend(Legend legend, List<LegendElement> legendSource){
        List<LegendElementDto> legendDTO = legendSource.stream().map(z-> {
            LegendElementDto dto = new LegendElementDto();
            dto.setCardNumber(z.getCardNumber());
            dto.setLegendSourceType(z.getLegendSourceType());
            dto.setId(z.getId());
            dto.setSource(z.getSource());
            dto.setPositionX(z.getPositionX());
            dto.setPositionY(z.getPositionY());
            dto.setImageId(z.getImageId());
            return dto;
        }).collect(Collectors.toList());

        LegendDTO dto = new LegendDTO();
        dto.setDeckId(legend.getId());
        dto.setTextSize(legend.getTextSize());
        //dto.setLegendTuned(deck.isLegendTuned());

        List<List<LegendElementDto>> data = new ArrayList<>();
        int size = legendDTO.stream().mapToInt(LegendElementDto::getCardNumber).max().getAsInt();
        for(int i=0;i<size+1;i++){
            data.add(new ArrayList<>());
        }

        legendDTO.forEach(z -> {
            int cardNumber = z.getCardNumber();
            data.get(cardNumber).add(z);
        });

        dto.setCards(data);

        return dto;
    }

}
