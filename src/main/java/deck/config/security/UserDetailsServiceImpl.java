package deck.config.security;

import com.google.common.collect.Lists;
import deck.crud.UserService;
import deck.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(user.getAuthority());
            user.setAuthorities(authorities);
            return user;
        }
        throw new UsernameNotFoundException(
                "Unable to find user with username provided!!");
    }

}