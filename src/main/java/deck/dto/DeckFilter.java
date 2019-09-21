package deck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import deck.model.Deck;
import deck.model.Deck_;
import deck.model.User;
import deck.model.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.Join;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckFilter {

    @Nullable
    private String search;

    public DeckFilter(@Nullable String search) {
        this.search = search;
    }

    public DeckFilter() {
    }

    public Specification<Deck> toSpecification(User user) {
        Specification<Deck> spec = (root, query, cb) -> {
            Join<Deck,User> userProd = root.join(Deck_.owner);
            return cb.or(cb.equal(root.get(Deck_.visibleAsPublic), true),
                    cb.equal(userProd.get(User_.id), user.getId()));
        };

        if (search != null) {
            String pattern = "%"+search+"%";
            Specification<Deck> cache = (root, query, cb) ->
                    cb.or(cb.like(root.get(Deck_.name), pattern),cb. like(root.get(Deck_.description), pattern));
            spec = cache.and(spec);
        }

        return spec;
    }

    @Nullable
    public String getSearch() {
        return search;
    }

    public void setSearch(@Nullable String search) {
        this.search = search;
    }
}
