package cinema.repository;

import cinema.model.Ticket;

public interface TicketRepository {

    void addToTicketTable(Ticket ticket);

    boolean removeFromTicketTable(Ticket ticket);

    int getIdFromTicketTable(Ticket ticket);
}
