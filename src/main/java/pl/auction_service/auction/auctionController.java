package pl.auction_service.auction;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auction")
@AllArgsConstructor
public class auctionController {

    private final AuctionService auctionService;

    @PutMapping
    public HttpStatus createAuction(Principal principal, @RequestBody SimpleAuction simpleAuction){
        return auctionService.createAuction(simpleAuction, principal.getName())? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @PatchMapping("/{id}")
    public HttpStatus bidding(Principal principal,@PathVariable long id, @RequestBody Bid bid) throws NotFoundException {
        int price = bid.getPrice();
        String username = principal.getName();
        return auctionService.bid(username, id, price)? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteAuction(Principal principal, @PathVariable long id){
        return auctionService.deleteAuction(id, principal.getName())? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    @GetMapping
    public List<Auction> getAllAuctions(){
        return auctionService.getAll();
    }

    @GetMapping("/{id}")
    public Auction getAuction(@PathVariable long id) throws NotFoundException {
        return auctionService.getAuction(id);
    }

}
