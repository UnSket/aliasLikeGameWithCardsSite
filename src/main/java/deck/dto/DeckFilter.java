package deck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import deck.model.Deck;
import deck.model.Deck_;
import deck.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

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
            return cb.or(cb.equal(root.get(Deck_.visibleAsPublic), true), cb.equal(root.get(Deck_.owner), user));
        };

        if (search != null) {
            Specification<Deck> cache = (root, query, cb) -> cb.like(root.get(Deck_.name), search);
            Specification<Deck> specFN = (root, query, cb) -> cb.like(root.get(Deck_.description), search);
            spec.and(cache.or(specFN));
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
