package pl.auction_service.auction;

import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.auction_service.user.User;
import pl.auction_service.user.UserRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public boolean createAuction(SimpleAuction simpleAuction, String username) {
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

    @Scheduled(fixedDelay = 500)
    private void scheduleFixedRateTaskAsync() throws InterruptedException {
        List<Auction> auctions = auctionRepository.findAllByIsFinished((byte) 0);
        Date date = new Date();
        for (Auction auction : auctions){
            if(auction.getTimeFinish().before(date)){
                auctionRepository.finish(auction.getId());
            }
        }
    }
}
