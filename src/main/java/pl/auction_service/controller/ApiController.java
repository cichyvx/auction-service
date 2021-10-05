package pl.auction_service.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.auction_service.user.User;
import pl.auction_service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
@CrossOrigin
public class ApiController {

    private final UserService userService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String all(){
        return "for all!";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String authorized(){
        return "for authorized";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admins(){
        return "for admins";
    }

    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    public String allUsers(){
        List<User> users = userService.getAllUsers();
        String r = "";
        for(User u : users)
            r += u.getUsername() +" | " + u.getPassword() + "</br>";
        return r;
    }

}
