package deck.repository;

import deck.model.Deck;
import deck.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long>, JpaSpecificationExecutor<Deck> {

    Integer countAllByOwner(User owner);
}
