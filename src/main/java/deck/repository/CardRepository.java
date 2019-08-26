package deck.repository;

import java.util.List;
import deck.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{

    List<Card> findAllByDeckId(long deckId);
}
