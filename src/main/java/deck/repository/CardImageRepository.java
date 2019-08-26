package deck.repository;

import deck.model.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardImageRepository extends JpaRepository<CardImage, Long> {

    List<CardImage> findAllByCardIdIn(List<Long> cardIds);
}
