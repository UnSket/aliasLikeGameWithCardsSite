package deck.repository;

import deck.model.CardImage;
import deck.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardImageRepository extends JpaRepository<CardImage, Long>{
}
