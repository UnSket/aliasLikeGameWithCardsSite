package deck.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

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

        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());\
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin123").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //TODO:open swagger-ui and h2;
        http.authorizeRequests()
                .antMatchers("/","/**").permitAll()
                .and()
             .authorizeRequests()
                .antMatchers("/h2/**", "/h2**", "/h2/**/**").permitAll()
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

                    ObjectMapper objectMapper = new ObjectMapper();

                    OutputStream out = new ByteArrayOutputStream();
                    objectMapper.writeValue(out, auth);
                    res.getWriter().append(out.toString());
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
                    res.getWriter().append("{\"message\":" + "\"" + errMsg + "\"}");
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
                .permitAll();
        // .and()
        // .csrf().disable(); // Disable CSRF support
    }


//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}