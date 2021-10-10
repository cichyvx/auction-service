package pl.auction_service.auction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "auction")
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
    private Date time_start;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time_finish;
    private int price;
    private Long best_user;
    private byte is_finished;
}
