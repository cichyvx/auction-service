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
    @Column(name = "user_id")
    private Long userId;
    private String title;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_start")
    private Date timeStart;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timeFinish")
    private Date timeFinish;
    private int price;
    @Column(name = "best_user")
    private Long bestUser;
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
