package cinema.service;

import cinema.model.Ticket;
import cinema.repository.TicketRepository;

public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public void addToDB(Ticket ticket) {
        ticketRepository.addToDB(ticket);
    }

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
}
