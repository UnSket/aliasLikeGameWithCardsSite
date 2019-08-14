package deck.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //TODO: user userDetailsService as source
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin123").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .authorizeRequests().antMatchers("/login**").permitAll()
                .and()
                .formLogin()
                // .loginPage("/login")
                .loginProcessingUrl("/api/perform_login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler((req, res, auth) -> {
                    for (GrantedAuthority authority : auth.getAuthorities()) {
                        System.out.println(authority.getAuthority());
                    }
                    System.out.println(auth.getName());

                    res.getWriter().append("{here will be json user}");
                    //res.sendRedirect("/");
                })
                //.defaultSuccessUrl("/")
                .failureHandler((req, res, exp) -> {
                    String errMsg = "";
                    if (exp.getClass().isAssignableFrom(BadCredentialsException.class)) {
                        errMsg = "Invalid username or password.";
                    } else {
                        errMsg = "Unknown error - " + exp.getMessage();
                    }
                    req.getSession().setAttribute("message", errMsg);
                    // res.sendRedirect("/login");
                })
                //.failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/signout")
                .logoutSuccessHandler((req, res, auth) -> {
                    req.getSession().setAttribute("message", "You are logged out successfully.");
                    res.sendRedirect("/login");
                })
                //.logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf().disable(); // Disable CSRF support

        http.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/h2/**").permitAll();
        http.headers().frameOptions().disable();

    }

}