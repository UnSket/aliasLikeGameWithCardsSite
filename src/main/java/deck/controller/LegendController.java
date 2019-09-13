package deck.controller;

import deck.crud.DeckService;
import deck.crud.LegendService;
import deck.dto.ImageTextConfigDTO;
import deck.dto.LegendDTO;
import deck.dto.LegendElementDto;
import deck.dto.UpdateLegendDto;
import deck.model.Deck;
import deck.model.LegendElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("api/legend/text/config")
    public ResponseEntity<Deck> updateDeckTextSize(@RequestBody ImageTextConfigDTO imageTextConfigDTO) {
        //TODO:refactor to service
        deckService.updateDeckTextSize(imageTextConfigDTO.getId(), imageTextConfigDTO.getSize());
        return ResponseEntity.ok(getLegend(imageTextConfigDTO.getId()));
    }

    @GetMapping(value = "/api/legend/{id}")
    public Deck getLegend(@PathVariable(value = "id") Long id) {
        Deck deck = deckService.getById(id);
        List<LegendElement> legendSource = legendService.getLegend(id);
        List<LegendElementDto> legend = legendSource.stream().map(z-> {
            LegendElementDto dto = new LegendElementDto();
            dto.setCardNumber(z.getCardNumber());
            dto.setLegendSourceType(z.getLegendSourceType());
            dto.setId(z.getId());
            dto.setSource(z.getContent());
            dto.setPositionX(z.getPositionX());
            dto.setPositionY(z.getPositionY());
            return dto;
        }).collect(Collectors.toList());

        LegendDTO dto = new LegendDTO();
        dto.setDeckId(id);
        dto.setTextSize(deck.getTextSize());

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
        deck.setLegend(dto);
        return deck;
    }

    @PostMapping(value = "/api/legend/update")
    public List<LegendElement> updateLegend(@RequestBody UpdateLegendDto updateLegendDto) {
        return legendService.setLegend(updateLegendDto);
    }

}
