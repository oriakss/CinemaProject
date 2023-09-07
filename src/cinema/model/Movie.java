package cinema.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
public class Movie {

    private int id;
    private final String title;
    private LocalDateTime date;
    private double ticketPrice;
    private final List<Ticket> ticketList;

    public int getUnPurchasedTickets() {
        int count = 0;
        for (Ticket ticket : ticketList) {
            if (ticket.isTicketBought()) {
                count++;
            }
        }
        return count;
    }

    public Movie(String title, LocalDateTime date) {
        this.title = title;
        this.date = date;
        ticketPrice = Math.round((new Random().nextDouble(10) + 10) * 100.0) / 100.0;
        ticketList = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            ticketList.add(new Ticket(null, title, i, ticketPrice, true));
        }
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", date=" + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) +
                '}';
    }
}
