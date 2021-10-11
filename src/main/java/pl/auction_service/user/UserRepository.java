package pl.auction_service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findUserByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.username = ?2, u.password = ?3 WHERE u.id = ?1")
    void updateUser(Long id, String username, String password);
}
