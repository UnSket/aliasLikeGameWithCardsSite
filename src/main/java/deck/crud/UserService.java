package deck.crud;

import java.util.List;
import deck.model.User;
import deck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) {
        List<User> allByEmailId = userRepository.findAllByEmailId(username);
        if(allByEmailId.size()!=1){
            throw new UsernameNotFoundException(username);
        }
        return allByEmailId.iterator().next();
    }
}
