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

    public boolean createAuction(SimpleAuction simpleAuction, String username) {
        if(simpleAuction.getStarterPrice() < 0 ||
            simpleAuction.getFinishDate() <= 0)
            return false;
        long userId = userRepository.findUserByUsername(username).getId();
        Date date = new Date();
        Auction auction = new Auction();
        auction.setUserId(userId);
        auction.setTitle(simpleAuction.getTitle());
        auction.setContent(simpleAuction.getContent());
        auction.setTimeStart(date);
        auction.setTimeFinish(new Date(System.currentTimeMillis() + ((long) simpleAuction.getFinishDate() * 60 * 1000)));
        auction.setPrice(simpleAuction.getStarterPrice());
        auction.setBestUser(userId);
        auction.setIsFinished((byte) 0);
        auctionRepository.save(auction);
        return true;
    }

    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    public Auction getAuction(Long id) throws NotFoundException {
        Auction auction = auctionRepository.getAuctionById(id);
        if(auction == null){
            throw new NotFoundException("cannot find Auction id: " + id);
        }
        return auction;
    }

    public boolean bid(String username, long id, int price) throws NotFoundException {
        if(price <= 0)
            return false;
        Auction auction = getAuction(id);
        User user = userRepository.findUserByUsername(username);
        if(auction == null || user == null){
            System.err.println("cannot find auction");
            return false;
        }

        if(auction.getTimeFinish().before(new Date()) ||
                auction.getUserId().equals(user.getId()) ||
                auction.getPrice() >= price){
            System.err.println("cannot Bid this auction");
            return false;
        }

        if(!user.getWallet().spendMoney(price)){
            System.err.println("not enough money");
            return false;
        }

        walletRepository.changeMoney(-price, user.getWallet().getId());
        auctionRepository.bid(user.getId(), price, id);
        return true;
    }


    public boolean deleteAuction(long id, String username){
        User user = userRepository.findUserByUsername(username);
        Auction auction = auctionRepository.getById(id);
        if (auction.getUserId() != user.getId())
            return false;
        auctionRepository.delete(auction);
        return true;
    }

    private void finalizeAuction(Auction auction){
        if(auction.getUserId() != auction.getBestUser()){
            User seller = auction.getOwner();
            walletRepository.changeMoney(auction.getPrice(), seller.getWallet().getId());
        }
        auctionRepository.finish(auction.getId());
    }

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
