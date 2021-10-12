package pl.auction_service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.auction_service.auction.Auction;
import pl.auction_service.wallet.Wallet;
import pl.auction_service.wallet.WalletRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    /**
     * @param username to be found
     * @return UserDetails that you search for
     * @throws UsernameNotFoundException if username dont exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("username: " + username + " not found");
        return user;
    }

//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }

    /**
     * @param username to be found
     * @return User that you search for
     */
    public User getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("username: " + username + " not found");
        return user;
    }

    /**
     * registry for new user and create new wallet for him
     * @param username
     * @param password
     * @return true if user was added of false if auction is failed for some reason
     */
    public boolean addUser(String username, String password) {
        //username must be unique
        try {
            getUserByUsername(username);
            return false;
        }catch (UsernameNotFoundException ignore){}
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRoles(1);
        userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUserId(getUserByUsername(username).getId());
        wallet.setMoney(0);
        walletRepository.save(wallet);
        return true;
    }

    /**
     * deleting user
     * @param username of user to be deleted
     * @return 200 if user was deleted or false if action failed for some reason
     */
    public boolean deleteUser(String username) {
        User user = getUserByUsername(username);
        userRepository.delete(user);
        return true;
    }

    /**
     * update username or password for user
     * @param userToEdit username of user to be updated
     * @param user user details to updated
     * @return true if user was updated or false if action failed for some reason
     */
    public boolean updateUser(String userToEdit, SimpleUser user) {
        User oldUser = getUserByUsername(userToEdit);
        userRepository.updateUser(oldUser.getId(), user.getUsername(), user.getPassword());
        return true;
    }

    /**
     * get list of all auction owning by specified user
     * @param username of user that we search for him auctions
     * @return list of all auction owned by specified user
     */
    public List<Auction> getUserAuction(String username) {
        User user = getUserByUsername(username);
        return user.getAuctions();
    }
}
