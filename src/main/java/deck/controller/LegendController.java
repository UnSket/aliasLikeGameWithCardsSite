package deck.controller;

import deck.crud.LegendService;
import deck.dto.LegendDTO;
import deck.dto.LegendElementDto;
import deck.dto.UpdateLegendDto;
import deck.model.LegendElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LegendController {

    private final LegendService legendService;

    @Autowired
    public LegendController(LegendService legendService) {
        this.legendService = legendService;
    }

    @GetMapping(value = "/api/legend/{id}")
    public LegendDTO getLegend(@PathVariable(value = "id") Long id) {
        List<LegendElement> legendSource = legendService.getLegend(id);
        List<LegendElementDto> legend = legendSource.stream().map(z-> {
            LegendElementDto dto = new LegendElementDto();
            dto.setCardNumber(z.getCardNumber());
            dto.setLegendSourceType(z.getLegendSourceType());
            dto.setId(z.getId());
            dto.setPositionX(z.getPositionX());
            dto.setPositionY(z.getPositionY());
            return dto;
        }).collect(Collectors.toList());

        LegendDTO dto = new LegendDTO();
        dto.setDeckId(id);
        dto.setTextSize(dto.getTextSize());

        List<List<LegendElementDto>> data = new ArrayList<>();
        int size = legend.stream().mapToInt(LegendElementDto::getCardNumber).max().getAsInt();
        for(int i=0;i<size;i++){
            data.add(new ArrayList<>());
        }

        legend.stream().forEach(z -> {
            int cardNumber = z.getCardNumber();
            data.get(cardNumber).add(z);
        });

        dto.setLegendElementDtos(data);
        return dto;
    }

    @PostMapping(value = "/api/legend/update")
    public List<LegendElement> updateLegend(@RequestBody UpdateLegendDto updateLegendDto) {
        return legendService.setLegend(updateLegendDto);
    }

}
