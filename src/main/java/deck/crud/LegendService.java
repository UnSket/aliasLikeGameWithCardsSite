package deck.crud;

import deck.dto.CreateLegendDTO;
import deck.dto.UpdateLegendDto;
import deck.model.Deck;
import deck.model.Legend;
import deck.repository.LegendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegendService {

    private LegendRepository legendRepository;
    private LegendElementService legendElementService;

    @Autowired
    public LegendService(LegendRepository legendRepository,
                         LegendElementService legendElementService) {
        this.legendRepository = legendRepository;
        this.legendElementService = legendElementService;
    }

    public Legend createLegend(Deck deck, CreateLegendDTO legendDTO) {
        Legend legend = new Legend();
        legend.setDeck(deck);
        legend.setName(legendDTO.getName());

        return legendRepository.save(legend);
    }

    public List<Legend> getDeckLegends(Long deckId) {
        List<Legend> legends = legendRepository.findAllByDeckId(deckId);

        return legends;
    }

    public Legend getLegendById(Long legendId) {
        return legendRepository.getOne(legendId);
    }

    public Legend updateLegend(UpdateLegendDto updateLegendDto) {
        Legend legend = legendRepository.getOne(updateLegendDto.getLegendId());
        legend.setTextSize(updateLegendDto.getTextSize());
        legend.setIsTuned(true);
        legendRepository.save(legend);
        legendElementService.updateLegendElements(legend, updateLegendDto);
        return legend;
    }

}
