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

    @GetMapping
    public int getMyWaller(Principal principal){
        return walletService.getMoneyFrom(principal.getName());
    }

    @PatchMapping
    public HttpStatus addMoney(Principal principal, @RequestBody SimpleWallet simpleWallet){
        if(simpleWallet.getMoney() <= 0)
            return HttpStatus.BAD_REQUEST;
        return walletService.changeMoney(simpleWallet.getMoney(), principal.getName())? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

}
