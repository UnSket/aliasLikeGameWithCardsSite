package deck.repository;

import deck.model.Legend;
import deck.model.LegendElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegendRepository extends JpaRepository<Legend, Long> {
    List<Legend> findAllByDeckId(Long deckId);
}
