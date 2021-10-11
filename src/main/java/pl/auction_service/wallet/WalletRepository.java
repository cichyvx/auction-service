package pl.auction_service.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Wallet findWalletByUserId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE wallet w SET w.money = ?1 WHERE w.id = ?2")
    void changeMoney(int money, long id);
}
