package pl.auction_service.auction;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.auction_service.user.User;
import pl.auction_service.user.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public boolean createAuction(Auction auction) {
        auctionRepository.save(auction);
        return true;
    }

    public List<Auction> getAll() {
        return auctionRepository.findAll();
    }

    public Optional<Auction> getAuction(Long id) {
        return auctionRepository.findById(id);
    }

    public List<Auction> getAllForUser(String userName) {
        return auctionRepository.findAllByUserId(userRepository.findUserByUsername(userName).getId());
    }

    public boolean bid(String username, long id, int price) {
        Optional<Auction> auction = auctionRepository.findById(id);
        assert auction.isPresent();
        User user = userRepository.findUserByUsername(username);
        assert user != null;
        if(auction.get().getTime_finish().before(new Date()) ||
                auction.get().getUserId().equals(user.getId()) ||
                auction.get().getPrice() >= price){
            System.err.println("cannot Bid this auction");
            return false;
        }
        auctionRepository.bid(user.getId(), price, id);
        return true;
    }

    @Scheduled(fixedDelay = 500)
    private void scheduleFixedRateTaskAsync() throws InterruptedException {
        List<Auction> auctions = auctionRepository.findAllByIsFinished((byte) 0);
        Date date = new Date();
        for (Auction auction : auctions){
            if(auction.getTime_finish().before(date)){
                auctionRepository.finish(auction.getId());
            }
        }
    }

}
