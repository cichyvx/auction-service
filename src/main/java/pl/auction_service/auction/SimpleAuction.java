package pl.auction_service.auction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleAuction {
    private String title;
    private String content;
    private int finishDate;
    private int starterPrice;

    /**
     * time in minutes (NOT MILISEC!) to auction end
     */
    public int getFinishDate() {
        return finishDate;
    }
}
