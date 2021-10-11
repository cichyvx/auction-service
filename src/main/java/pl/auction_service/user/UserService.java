package pl.auction_service.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("username: " + username + " not found");
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("username: " + username + " not found");
        return user;
    }

    public boolean addUser(String username, String password) {
        try {
            getUserByUsername(username);
            return false;
        }catch (UsernameNotFoundException ignore){}
        User user = new User();
        user.setUsername(username);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRoles(0);
        userRepository.save(user);
        Wallet wallet = new Wallet();
        wallet.setUserId(getUserByUsername(username).getId());
        wallet.setMoney(0);
        walletRepository.save(wallet);
        return true;
    }

    public boolean deleteUser(String username) {
        User user = getUserByUsername(username);
        userRepository.delete(user);
        return true;
    }

    public boolean updateUser(String userToEdit, SimpleUser user) {
        User oldUser = getUserByUsername(userToEdit);
        userRepository.updateUser(oldUser.getId(), user.getUsername(), user.getPassword());
        return true;
    }

    public List<Auction> getUserAuction(String name) {
        User user = getUserByUsername(name);
        return user.getAuctions();
    }
}
