package pl.auction_service.config.security;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.auction_service.user.User;
import pl.auction_service.user.UserService;

@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider{

    private final UserService service;

    public CustomAuthenticationProvider(UserService service, PasswordEncoder passwordEncoder){
        this.service = service;
        setUserDetailsService(this.service);
        setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User mayBeUser = service.getUserByUsername(username);

        if(mayBeUser == null)
            throw new AuthenticationCredentialsNotFoundException("user not found");
        if(!getPasswordEncoder().matches(password, mayBeUser.getPassword()))
            throw new AuthenticationCredentialsNotFoundException("wrong password");
        return new UsernamePasswordAuthenticationToken(username, password, null);
    }
}
