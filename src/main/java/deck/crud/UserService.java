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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        user.setAuthority("ROLE_USER");
        user.setEmail(createUserDTO.getEmail());
        user.setUsername(createUserDTO.getUsername());
        user.setActive(createUserDTO.isActive());
        if (createUserDTO.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(createUserDTO.getPassword()));
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getUsername()));
        }
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
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public User findUserByUsername(String username) {
        List<User> allByEmailId = userRepository.findAllByUsername(username);
        if (allByEmailId.size() != 1) {
            throw new UsernameNotFoundException(username);
        }
        return allByEmailId.iterator().next();
    }
}
