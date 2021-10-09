package pl.auction_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.auction_service.user.SimpleUser;
import pl.auction_service.user.UserService;

@RestController
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PutMapping("/register")
    public HttpStatus register(@RequestBody SimpleUser simpleUser){
        return userService.addUser(simpleUser.getUsername(), simpleUser.getPassword()) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

}
