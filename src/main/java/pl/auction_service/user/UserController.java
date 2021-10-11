package pl.auction_service.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.auction_service.auction.Auction;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public User myAccount(Principal principal){
        return userService.getUserByUsername(principal.getName());
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    @GetMapping("/myAuction")
    public List<Auction> myAuction(Principal principal){
        return userService.getUserAuction(principal.getName());
    }

    @GetMapping("userAuction/{username}")
    public List<Auction> getUserAuction(@PathVariable String username){
        return userService.getUserAuction(username);
    }

    @PutMapping
    public HttpStatus registerUser(@RequestBody SimpleUser simpleUser){
        return userService.addUser(simpleUser.getUsername(), simpleUser.getPassword()) ? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @DeleteMapping
    public HttpStatus deleteUser(@RequestBody SimpleUser simpleUser){
        return userService.deleteUser(simpleUser.getUsername()) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @PatchMapping("/{username}")
    public HttpStatus updateUser(@PathVariable String username, @RequestBody SimpleUser user){
        return userService.updateUser(username, user)? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

}
