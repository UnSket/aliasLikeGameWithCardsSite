package deck.config;

import deck.crud.UserService;
import deck.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {


    final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findUserByUsername(username);
        if (user != null) {

            String password = user.getPassword();
            boolean enabled = user.isActive();
            boolean accountNonExpired = user.isActive();
            boolean credentialsNonExpired = user.isActive();
            boolean accountNonLocked = user.isActive();

            Collection<GrantedAuthority> authorities = new ArrayList<>();
            //for (Role r : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
            //}
            return new org.springframework.security.core.userdetails.User(
                    username, password, enabled, accountNonExpired,
                    credentialsNonExpired, accountNonLocked, authorities);
        }
        throw new UsernameNotFoundException(
                "Unable to find user with username provided!!");
    }

}