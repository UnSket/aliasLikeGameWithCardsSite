package deck.repository;

import deck.model.ImageMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetaRepository extends JpaRepository<ImageMeta, Long>{

}
