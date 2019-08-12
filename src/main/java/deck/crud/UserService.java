package deck.crud;

import deck.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User findUserByUsername(String username) {
        User user = new User();
        user.setEmailId("user");
        user.setPassword("password");
        user.setActive(true);
        return null;
    }
}
