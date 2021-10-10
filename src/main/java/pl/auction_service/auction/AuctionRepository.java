package pl.auction_service.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findAllByUserId(long id);

    @Modifying
    @Query("UPDATE auction a set a.best_user = ?1, a.price = ?2 WHERE a.id = ?3")
    @Transactional
    void bid(Long userId, int price, Long auctionId);
}
