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

    private User getUser(String username){
        return userRepository.findUserByUsername(username);
    }

    private Wallet getWalletFromUser(String username){
        return getUser(username).getWallet();
    }

    public int getMoneyFrom(String name) {
        return getUser(name).getWallet().getMoney();
    }

    public boolean changeMoney(int money, String username) {
        Wallet wallet = getWalletFromUser(username);
        if(money < 0){
            if(!wallet.spendMoney(money))
                return false;
        }
        else
            wallet.addMoney(money);

        walletRepository.changeMoney(wallet.getMoney(), wallet.getId());
        return true;
    }
}
