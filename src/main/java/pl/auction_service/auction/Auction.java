package pl.auction_service.auction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import pl.auction_service.user.User;

import javax.persistence.*;
import java.util.Date;


@Entity(name = "auction")
@Table(name = "auction")
@Getter
@Setter
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //auction owner id
    @Column(name = "user_id")
    private Long userId;

    //name of the auction
    private String title;

    //description text
    private String content;

    //time when the auction was created
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_start")
    private Date timeStart;

    //time when the auction is finished
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timeFinish")
    private Date timeFinish;

    //current highest offer
    private int price;

    //the user who made the best offer, if id was same as owner id it means that nobody make offer
    @Column(name = "best_user")
    private Long bestUser;

    //1 if auction is ended or 0 is in progress
    @Column(name = "is_finished")
    private byte isFinished;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User owner;

    public User getOwner() {
        return owner;
    }
}
