package deck.repository;

import deck.model.LegendElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface LegendElementRepository extends JpaRepository<LegendElement, Long> {

    List<LegendElement> findAllByLegendId(Long legendId);

    List<LegendElement> findAllByImageId(Long imageId);

    void deleteAllByLegendId(Long legendId);
}
