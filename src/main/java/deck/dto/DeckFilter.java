package deck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import deck.model.Deck;
import deck.model.Deck_;
import deck.model.User;
import deck.model.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckFilter {

    @Nullable
    private String name;

    @Nullable
    private String description;


    public DeckFilter() {
    }

    public Specification<Deck> toSpecification(User user) {

        Specification<Deck> spec = (root, query, cb) -> {
            return cb.or(cb.equal(root.get(Deck_.visibleAsPublic), true), cb.equal(root.get(Deck_.owner), user));
        };

        if (name != null) {
            Specification<Deck> cache = (root, query, cb) -> cb.like(root.get(Deck_.name), name);
            spec.and(cache);
        }

        if (description != null) {
            Specification<Deck> specFN = (root, query, cb) -> cb.like(root.get(Deck_.description), description);
            spec.and(specFN);
        }

        return spec;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
