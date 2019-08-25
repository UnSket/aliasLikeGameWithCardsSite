package deck.repository;

import deck.model.Card;
import deck.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{
}
