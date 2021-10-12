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

    /**
     * @param principal logged user
     * @return info about account that you be current logged
     */
    @GetMapping
    public User myAccount(Principal principal){
        return userService.getUserByUsername(principal.getName());
    }

    /**
     *
     * @param username user that we search
     * @return user specified in URL param
     */
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username){
        return userService.getUserByUsername(username);
    }

    /**
     * @param principal current logged user
     * @return list of auctions that the user owns
     */
    @GetMapping("/myAuction")
    public List<Auction> myAuction(Principal principal){
        return userService.getUserAuction(principal.getName());
    }

    /**
     * @param username username of user that we search for his auction
     * @return return all auction owning fo specified user
     */
    @GetMapping("userAuction/{username}")
    public List<Auction> getUserAuction(@PathVariable String username){
        return userService.getUserAuction(username);
    }

    /**
     * creating new user
     * @param simpleUser new user username and password
     * @return 200 if user was added or 400 if action failed
     */
    @PutMapping
    public HttpStatus registerUser(@RequestBody SimpleUser simpleUser){
        return userService.addUser(simpleUser.getUsername(), simpleUser.getPassword()) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    /**
     * deleting user
     * this can be possible only if you be logged as user with role ADMIN
     * @param simpleUser user to be deleted
     * @return 200 if user was deleted or 400 if action failed
     */
    @DeleteMapping
    public HttpStatus deleteUser(@RequestBody SimpleUser simpleUser){
        return userService.deleteUser(simpleUser.getUsername()) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    /**
     *
     * @param username user that we want details update
     * @param user details to change, if you want to don't change one of them you must write it equal as current
     * @return 200 if details was updated or 400 if action failed
     */
    @PatchMapping("/{username}")
    public HttpStatus updateUser(@PathVariable String username, @RequestBody SimpleUser user){
        return userService.updateUser(username, user)? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

}
