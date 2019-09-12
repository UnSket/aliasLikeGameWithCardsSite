package deck.controller;

import deck.crud.LegendService;
import deck.dto.UpdateLegendDto;
import deck.model.LegendElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LegendController {

    private final LegendService legendService;

    @Autowired
    public LegendController(LegendService legendService) {
        this.legendService = legendService;
    }

    @GetMapping(value = "/api/legend/{id}")
    public List<LegendElement> getLegend(@PathVariable(value = "id") Long id) {
        return legendService.getLegend(id);
    }

    @PostMapping(value = "/api/legend/update")
    public List<LegendElement> updateLegend(@RequestBody UpdateLegendDto updateLegendDto) {
        return legendService.setLegend(updateLegendDto);
    }

}
