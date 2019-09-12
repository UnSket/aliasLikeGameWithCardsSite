package deck.controller;

import deck.crud.LegendService;
import deck.model.LegendElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
