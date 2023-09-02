package cinema.service;

import cinema.model.Ticket;

public interface TicketService {

    void addToTicketTable(Ticket ticket);

    void removeFromTicketTable(Ticket ticket);

    int getIdFromTicketTable(Ticket ticket);
}
