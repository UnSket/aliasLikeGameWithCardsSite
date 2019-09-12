package deck.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import deck.model.User;
import deck.model.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFilter {

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    @Nullable
    private String email;

    @Nullable
    private String password;

    @Nullable
    private boolean active;

    public UserFilter() {
    }

    public Specification<User> toSpecification() {

        Specification<User> spec = (root, query, cb) -> {
            return cb.equal(root.get(User_.active), true);
        };

        if (firstName != null) {
            Specification<User> cache = (root, query, cb) -> cb.like(root.get(User_.firstName), firstName);
            spec.and(cache);
        }

        if (lastName != null) {
            Specification<User> specFN = (root, query, cb) -> cb.like(root.get(User_.lastName), lastName);
            spec.and(specFN);
        }

        if (email != null) {
            Specification<User> specFN = (root, query, cb) -> cb.like(root.get(User_.emailId), email);
            spec.and(specFN);
        }

        return spec;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
