package cinema.service;

import cinema.model.Ticket;
import cinema.repository.TicketRepository;

public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public void addToTicketTable(Ticket ticket) {
        ticketRepository.addToTicketTable(ticket);
    }

    @Override
    public void removeFromTicketTable(Ticket ticket) {
        ticketRepository.removeFromTicketTable(ticket);
    }

    @Override
    public int getIdFromTicketTable(Ticket ticket) {
        return ticketRepository.getIdFromTicketTable(ticket);
    }

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
