package pl.auction_service.wallet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.auction_service.user.User;
import pl.auction_service.user.UserRepository;

@Service
@AllArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    /**
     * get user by him username
     * @param username
     * @return user
     */
    private User getUser(String username){
        return userRepository.findUserByUsername(username);
    }

    /**
     *
     * @param username wallet owner name
     * @return wallet owner
     */
    private Wallet getWalletFromUser(String username){
        return getUser(username).getWallet();
    }

    /**
     * @param username wallet owner name
     * @return amount of monet form wallet
     */
    public int getMoneyFrom(String username) {
        return getUser(username).getWallet().getMoney();
    }

    /**
     * add or spend money
     * if you want to minus money just make money param negative
     * @param money to be added or spend
     * @param username name of user for this operation
     * @return true if operation is ok or false if operation failed
     */
    public boolean changeMoney(int money, String username) {
        Wallet wallet = getWalletFromUser(username);
        //wallet mus exist
        if(wallet == null){
            return false;
        }

        //spending money
        if(money < 0){
            //user must have enough money
            if(wallet.getMoney() - money < 0)
                return false;
            wallet.setMoney(wallet.getMoney() - money);
        }
        //adding money
        else
            wallet.setMoney(wallet.getMoney() + money);

        walletRepository.changeMoney(wallet.getMoney(), wallet.getId());
        return true;
    }
}
