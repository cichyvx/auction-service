package pl.auction_service.auction;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SimpleAuction {
    private String title;
    private String content;
    /**
     * time in minutes from now to end
     */
    private int finishDate;
    private int starterPrice;
}
