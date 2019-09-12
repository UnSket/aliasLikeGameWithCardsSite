package deck.controller;

import deck.crud.UserService;
import deck.dto.ActivateUserDTO;
import deck.dto.CreateUserDTO;
import deck.dto.UserFilter;
import deck.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/api/currentUser")
    public ResponseEntity currentUser() {
        try {
            User user = userService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping(value = "/api/activateUser")
    public User createUser(@RequestBody ActivateUserDTO activateUserDTO) {
        return userService.activateUser(activateUserDTO);
    }

    @PostMapping(value = "/api/createUser")
    public User createUser(@RequestBody CreateUserDTO createUserDTO) {
        return userService.createUser(createUserDTO);
    }

    @PostMapping(value = "/api/users")
    public Page<User> getUsers(@NotNull final Pageable pageable, @RequestBody UserFilter filter) {
        return userService.find(pageable, filter);
    }

}
