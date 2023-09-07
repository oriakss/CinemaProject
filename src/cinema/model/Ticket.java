package cinema.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Ticket {

    private int id;
    private String user;
    private String movie;
    private int seatNum;
    private double price;
    private boolean ticketBought;

    public Ticket(String user, String movie, int seatNum, double price, boolean ticketBought) {
        this.user = user;
        this.movie = movie;
        this.seatNum = seatNum;
        this.price = price;
        this.ticketBought = ticketBought;
    }

    @Override
    public String toString() {
        return "\nMovie: " + movie +
                "\nSeat num: " + seatNum +
                "\nPrice: " + price;
    }
}
