package pl.auction_service.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.auction_service.user.UserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableScheduling
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new CustomAuthenticationProvider(userService, bCryptPasswordEncoder()));
//        .inMemoryAuthentication()
//            .withUser("user").password(bCryptPasswordEncoder().encode("pass")).roles("")
//            .and()
//            .withUser("admin").password(bCryptPasswordEncoder().encode("pass")).roles("ADMIN")
//        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //.cors().disable()

                .httpBasic()

                .and().formLogin().loginProcessingUrl("/login").permitAll().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.PUT, "/user").permitAll()
                .antMatchers(HttpMethod.PUT, "/auction/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/auction/*").authenticated()
                .antMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/user/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/auction/**").authenticated()
                .antMatchers(HttpMethod.GET, "/auction/**").permitAll()
                .antMatchers(HttpMethod.GET, "/user/","/user/userAuction/*").permitAll()
                .antMatchers(HttpMethod.GET, "/user", "/user/myAuction").authenticated()
                .antMatchers(HttpMethod.GET, "/login").permitAll()
                .anyRequest().authenticated();
    }
}
