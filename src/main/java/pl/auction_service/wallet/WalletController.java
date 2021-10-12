package pl.auction_service.wallet;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/wallet")
@AllArgsConstructor
public class WalletController {

    private final WalletService walletService;

    /**
     * @param principal current logged user
     * @return wallet owned by current logged user
     */
    @GetMapping
    public int getMyWaller(Principal principal){
        return walletService.getMoneyFrom(principal.getName());
    }

    /**
     * adding money for user
     * @param principal current logged for user
     * @param simpleWallet amount to be added
     * @return 200 if money was added or 400 if auction failed
     */
    @PatchMapping
    public HttpStatus addMoney(Principal principal, @RequestBody SimpleWallet simpleWallet){
        if(simpleWallet.getMoney() <= 0)
            return HttpStatus.BAD_REQUEST;
        return walletService.changeMoney(simpleWallet.getMoney(), principal.getName())? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

}
