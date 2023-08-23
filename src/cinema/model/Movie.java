package cinema.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Movie {

    private int id;
    private final String title;
    private final LocalDateTime date;
    private List<Ticket> ticketList;

    public int getUnPurchasedTickets() {
        int count = 0;
        for (Ticket ticket : ticketList) {
            if (ticket.getIsNotBought()) {
                count++;
            }
        }
        return count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public Movie(int id, String title, LocalDateTime date, List<Ticket> ticketList) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.ticketList = ticketList;
    }

    public Movie(String title, LocalDateTime date) {
        this.title = title;
        this.date = date;
        ticketList = new ArrayList<>();
        double price = Math.round((new Random().nextDouble(10) + 10) * 100.0) / 100.0;
        for (int i = 1; i <= 50; i++) {
            ticketList.add(new Ticket(null, this, i, price, true));
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
