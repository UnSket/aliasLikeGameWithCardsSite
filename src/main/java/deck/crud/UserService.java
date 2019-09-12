package deck.crud;

import deck.dto.ActivateUserDTO;
import deck.dto.CreateUserDTO;
import deck.dto.UserFilter;
import deck.model.User;
import deck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof User) {
                        return (User) authentication.getPrincipal();
                    } else {
                        return null;
                    }
                }).orElseThrow(() -> new EntityNotFoundException("Logged user not found"));
    }


    public User createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setActive(false);
        if (createUserDTO.getPassword() != null) {
            user.setPassword(createUserDTO.getPassword());
        } else {
            user.setPassword(user.getEmailId());
        }
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        return userRepository.save(user);
    }

    public User activateUser(ActivateUserDTO activateUserDTO){
        Optional<User> byId = userRepository.findById(activateUserDTO.getUserId());
        if (!byId.isPresent()) {
            throw new EntityNotFoundException("User with id " + activateUserDTO.getUserId() + " not found");
        }
        User user = byId.get();
        if(activateUserDTO.getActive()){
            return activateUser(user);
        }
        return disactivateUser(user);
    }
    private User activateUser(User user) {
        user.setActive(true);
        return userRepository.save(user);
    }

    private User disactivateUser(User user) {
        user.setActive(false);
        return userRepository.save(user);
    }

    public Page<User> find(Pageable pageable, UserFilter userFilter) {
        return userRepository.findAll(userFilter.toSpecification(), pageable);
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUsername(String username) {
        List<User> allByEmailId = userRepository.findAllByEmailId(username);
        if (allByEmailId.size() != 1) {
            throw new UsernameNotFoundException(username);
        }
        return allByEmailId.iterator().next();
    }
}
