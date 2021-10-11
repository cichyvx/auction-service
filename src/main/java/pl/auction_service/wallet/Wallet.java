package pl.auction_service.wallet;

import lombok.Getter;
import lombok.Setter;
import pl.auction_service.user.User;

import javax.persistence.*;

@Entity(name = "wallet")
@Table(name = "wallet")
@Getter
public class Wallet {

    @Id
    private long id;
    @Column(name = "user_id")
    private long userId;
    private int money;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            insertable = false,
            updatable = false
    )
    private User owner;

    public void addMoney(int money){
        this.money += money;
    }

    public boolean spendMoney(int money){
        if(this.money - money >= 0){
            this.money -= money;
            return true;
        }
        return false;
    }

}
