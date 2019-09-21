package deck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import deck.model.User;
import deck.model.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilter {

    @Nullable
    private String search;

    @Nullable
    private Boolean active;

    public UserFilter() {
    }

    public Specification<User> toSpecification() {

        Specification<User> spec = (root, query, cb) -> cb.conjunction();

        if (search != null) {
            search = "%"+ search +"%";
            Specification<User> specFN = (root, query, cb) -> cb.like(root.get(User_.emailId), search);
            spec.and(specFN);
        }

        if (active != null) {
            Specification<User> cache = (root, query, cb) -> cb.equal(root.get(User_.active), active);
            spec.and(cache);
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
