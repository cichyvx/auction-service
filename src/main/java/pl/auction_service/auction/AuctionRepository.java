package pl.auction_service.auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findAllByUserId(long id);

    @Modifying
    @Query("UPDATE auction a SET a.best_user = ?1, a.price = ?2 WHERE a.id = ?3")
    @Transactional
    void bid(Long userId, int price, Long auctionId);

    @Modifying
    @Query("UPDATE auction a SET a.isFinished = 1 WHERE a.id = ?1")
    @Transactional
    void finish(Long id);

    List<Auction> findAllByIsFinished(Byte isFinished);

}
