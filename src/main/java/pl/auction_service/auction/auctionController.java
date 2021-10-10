package pl.auction_service.auction;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.auction_service.user.UserService;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auction")
@AllArgsConstructor
public class auctionController {

    private final AuctionService auctionService;
    private final UserService userService;

    @PutMapping
    public HttpStatus createAuction(Principal principal, @RequestBody SimpleAuction simpleAuction){
        String username = principal.getName();
        long userId = userService.getUserByUsername(username).getId();
        Date date = new Date();
        Auction auction = new Auction();
        auction.setUserId(userId);
        auction.setTitle(simpleAuction.getTitle());
        auction.setContent(simpleAuction.getContent());
        auction.setTime_start(date);
        auction.setTime_finish(new Date(System.currentTimeMillis() + ((long) simpleAuction.getFinishDate() * 60 * 1000)));
        auction.setPrice(simpleAuction.getStarterPrice());
        auction.setBest_user(userId);
        auction.setIsFinished((byte) 0);
        return auctionService.createAuction(auction)? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @PatchMapping("/{id}")
    public HttpStatus bidding(Principal principal,@PathVariable long id, @RequestBody Bid bid){
        int price = bid.getPrice();
        String username = principal.getName();
        return auctionService.bid(username, id, price)? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @GetMapping
    public List<Auction> getAllAuctions(){
        return auctionService.getAll();
    }

    @GetMapping("/{id}")
    public Auction getAuction(@PathVariable long id) {
        Optional<Auction> auction = auctionService.getAuction(id);
        return auction.get();
    }

    @GetMapping("/forUser/{user_name}")
    public List<Auction> getAllAuctionForUser(@PathVariable String user_name){
        return auctionService.getAllForUser(user_name);
    }


}
