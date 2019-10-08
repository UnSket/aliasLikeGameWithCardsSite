package deck.controller;

import deck.crud.DeckService;
import deck.crud.LegendService;
import deck.dto.LegendDTO;
import deck.dto.LegendElementDto;
import deck.dto.UpdateLegendDto;
import deck.model.Deck;
import deck.model.LegendElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LegendController {

    private final LegendService legendService;
    private final DeckService deckService;

    @Autowired
    public LegendController(LegendService legendService,
                            DeckService deckService) {
        this.legendService = legendService;
        this.deckService = deckService;
    }

    @Transactional
    @GetMapping(value = "/api/legend/{id}")
    public LegendDTO getLegend(@PathVariable(value = "id") Long id) {
        Deck deck = deckService.getById(id);
        List<LegendElement> legendSource = legendService.getLegend(id);
        return fromLegend(deck, legendSource);
    }

    @PostMapping(value = "/api/legend/update")
    public LegendDTO updateLegend(@RequestBody UpdateLegendDto updateLegendDto) {
        Deck deck = deckService.getById(updateLegendDto.getDeckId());
        List<LegendElement> legendElements = legendService.setLegend(deck, updateLegendDto);
        deckService.updateDeckTextSize(updateLegendDto.getDeckId(), updateLegendDto.getTextSize());

        return fromLegend(deck, legendElements);
    }

    private LegendDTO fromLegend(Deck deck, List<LegendElement> legendSource){
        List<LegendElementDto> legend = legendSource.stream().map(z-> {
            LegendElementDto dto = new LegendElementDto();
            dto.setCardNumber(z.getCardNumber());
            dto.setLegendSourceType(z.getLegendSourceType());
            dto.setId(z.getId());
            dto.setSource(z.getContent());
            dto.setPositionX(z.getPositionX());
            dto.setPositionY(z.getPositionY());
            dto.setImageId(z.getImageId());
            return dto;
        }).collect(Collectors.toList());

        LegendDTO dto = new LegendDTO();
        dto.setDeckId(deck.getId());
        dto.setTextSize(deck.getTextSize());
        dto.setLegendTuned(deck.isLegendTuned());

        List<List<LegendElementDto>> data = new ArrayList<>();
        int size = legend.stream().mapToInt(LegendElementDto::getCardNumber).max().getAsInt();
        for(int i=0;i<size+1;i++){
            data.add(new ArrayList<>());
        }

        legend.forEach(z -> {
            int cardNumber = z.getCardNumber();
            data.get(cardNumber).add(z);
        });

        dto.setCards(data);

        return dto;
    }

}
