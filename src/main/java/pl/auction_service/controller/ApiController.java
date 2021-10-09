package pl.auction_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.auction_service.user.User;
import pl.auction_service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
@CrossOrigin
public class ApiController {

    private final UserService userService;

    @GetMapping("/all")
    public String all(){
        return "for all!";
    }

    @GetMapping("/user")
    public String authorized(){
        return "for authorized";
    }

    @GetMapping("/user")
    public String admins(){
        return "for admins";
    }

    @GetMapping("/allusers")
    public String allUsers(){
        List<User> users = userService.getAllUsers();
        String r = "";
        for(User u : users)
            r += u.getUsername() +" | " + u.getPassword() + "</br>";
        return r;
    }

}
