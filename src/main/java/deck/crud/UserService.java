package deck.crud;

import java.util.List;
import java.util.Optional;

import deck.model.User;
import deck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {

    final UserRepository userRepository;

    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof User) {
                        User springSecurityUser = (User) authentication.getPrincipal();
                        return springSecurityUser;
                    } else {
                        return null;
                    }
                }).orElseThrow(() -> new EntityNotFoundException("Logged user not found"));
    }

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
