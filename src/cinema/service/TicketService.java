package cinema.service;

import cinema.model.Ticket;

public interface TicketService {

    void addToTicketTable(Ticket ticket);

    boolean removeFromTicketTable(Ticket ticket);

    int getIdFromTicketTable(Ticket ticket);
}
