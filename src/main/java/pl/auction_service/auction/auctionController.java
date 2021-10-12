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

    /**
     * creating a new auction belonging to the logged in user
     * if the user is not logged in, the method throw error
     * @param principal Auction owner
     * @param simpleAuction basic details about auction
     * @return return 200 if operation is ok or 400 if a problem occurred
     */
    @PutMapping
    public HttpStatus createAuction(Principal principal, @RequestBody SimpleAuction simpleAuction){
        return auctionService.createAuction(simpleAuction, principal.getName())? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    /**
     * bidding selected auction
     * @param principal logged user that bidding selected auction
     * @param id id of auction to be auctioned off
     * @param bid user offer
     * @return return 200 if operation is ok or 400 if a problem occurred
     * @throws NotFoundException if any auction has the id specified in the parameter
     */
    @PatchMapping("/{id}")
    public HttpStatus bidding(Principal principal,@PathVariable long id, @RequestBody Bid bid) throws NotFoundException {
        return auctionService.bid(principal.getName(), id, bid.getPrice())? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    /**
     * deleting selected auction
     * possible only if user has ADMIN role
     * @param principal logged user
     * @param id auction id to be deleted
     * @return return 200 if operation is ok or 400 if a problem occurred
     */
    @DeleteMapping("/{id}")
    public HttpStatus deleteAuction(Principal principal, @PathVariable long id){
        return auctionService.deleteAuction(id, principal.getName())? HttpStatus.OK : HttpStatus.NO_CONTENT;
    }

    /**
     *
     * @return list of all auctions
     */
    @GetMapping
    public List<Auction> getAllAuctions(){
        return auctionService.getAll();
    }

    /**
     * one auction
     * @param id auction id
     * @return auction specified in url param
     * @throws NotFoundException if any of auction don't have specified id
     */
    @GetMapping("/{id}")
    public Auction getAuction(@PathVariable long id) throws NotFoundException {
        return auctionService.getAuction(id);
    }

}
