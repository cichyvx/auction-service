package pl.auction_service.auction;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.auction_service.user.User;
import pl.auction_service.user.UserRepository;
import pl.auction_service.wallet.WalletRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    /**
     * creating a new auction
     * @param simpleAuction details of new auction
     * @param username name of user creating auction
     * @return true if auction was created or false if operation failed
     */
    public boolean createAuction(SimpleAuction simpleAuction, String username) {

        //starting price must bo positive number
        if(simpleAuction.getStarterPrice() < 0 ||
            simpleAuction.getFinishDate() <= 0)
            return false;

        User user = userRepository.findUserByUsername(username);
        //user must exist
        if(user == null){
            return false;
        }

        long userId = user.getId();
        Date date = new Date();
        Auction auction = new Auction();
        auction.setUserId(userId);
        auction.setTitle(simpleAuction.getTitle());
        auction.setContent(simpleAuction.getContent());
        auction.setTimeStart(date);

        //making milliseconds from minutes
        auction.setTimeFinish(new Date(System.currentTimeMillis() + ((long) simpleAuction.getFinishDate() * 60 * 1000)));
        auction.setPrice(simpleAuction.getStarterPrice());
        auction.setBestUser(userId);
        auction.setIsFinished((byte) 0);
        auctionRepository.save(auction);
        return true;
    }

    /**
     *
     * @return list of all existing auction
     */
    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    /**
     *
     * @param id of selected auction
     * @return auction with specified id
     * @throws NotFoundException if auction not exist
     */
    public Auction getAuction(Long id) throws NotFoundException {
        Auction auction = auctionRepository.getAuctionById(id);
        if(auction == null){
            throw new NotFoundException("cannot find Auction id: " + id);
        }
        return auction;
    }

    /**
     * this method is used for making offer for auction
     * @param username name of user who trying to make offer
     * @param id id of auction to be auctioned off
     * @param price the proposed amount
     * @return true if offer is accepted or false if for some reasons, the action failed
     * @throws NotFoundException if user or auction don't exist
     */
    public boolean bid(String username, long id, int price) throws NotFoundException {

        // the amount must be a positive number
        if(price <= 0)
            return false;
        Auction auction = getAuction(id);
        User user = userRepository.findUserByUsername(username);

        //bidding user and auction must exist
        if(auction == null || user == null){
            System.err.println("cannot find auction or user");
            return false;
        }

        //auction cannot be finished
        //user cannot bid owned auction
        //price must be higher than previous
        if(auction.isFinished() ||
                auction.getUserId().equals(user.getId()) ||
                auction.getPrice() >= price){
            System.err.println("cannot Bid this auction");
            return false;
        }

        //the amount cannot be greater than the amount of money you have
        if(user.getWallet().getMoney() - price < 0){
            System.err.println("not enough money");
            return false;
        }

        walletRepository.changeMoney(-price, user.getWallet().getId());
        auctionRepository.bid(user.getId(), price, id);
        return true;
    }


    /**
     *
     * @param id auction id
     * @param username name of auction owner
     * @return true if deleting passed or false if operation failed
     */
    public boolean deleteAuction(long id, String username){
        User user = userRepository.findUserByUsername(username);
        Auction auction = auctionRepository.getById(id);

        //user and auction must exist
        if(user == null || auction == null){
            return false;
        }

        //auction owner must be same as user trying to delete this auction
        if (auction.getUserId() != user.getId())
            return false;
        auctionRepository.delete(auction);
        return true;
    }

    /**
     * closing auction and getting paid for them
     * @param auction auction to be closed
     */
    private void finalizeAuction(Auction auction){
        // if auction be bid at last one time getting money for their owner or if anybody making offer do nothing
        if(auction.getUserId() != auction.getBestUser()){
            User seller = auction.getOwner();
            walletRepository.changeMoney(auction.getPrice(), seller.getWallet().getId());
        }
        auctionRepository.finish(auction.getId());
    }

    /**
     * this method automatically checks every half a second if there is any auction that has expired
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 500)
    private void scheduleFixedRateTaskAsync() throws InterruptedException {
        List<Auction> auctions = auctionRepository.findAllByIsFinished((byte) 0);
        Date date = new Date();
        for (Auction auction : auctions){
            if(auction.getTimeFinish().before(date)){
                finalizeAuction(auction);
            }
        }
    }
}
