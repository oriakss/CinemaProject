package cinema.controller;

import cinema.model.Ticket;
import cinema.repository.MovieRepository;
import cinema.repository.MovieRepositoryImpl;
import cinema.repository.TicketRepository;
import cinema.repository.TicketRepositoryImpl;
import cinema.service.MovieService;
import cinema.service.MovieServiceImpl;
import cinema.service.TicketService;
import cinema.service.TicketServiceImpl;

import java.util.List;

import static cinema.controller.GlobalController.movieCatalog;
import static cinema.controller.GlobalController.user;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.ScannerUtil.SCANNER;

public class UserController {

    private static final TicketRepository TICKET_REPOSITORY = new TicketRepositoryImpl();
    private static final TicketService TICKET_SERVICE = new TicketServiceImpl(TICKET_REPOSITORY);
    private static final MovieRepository MOVIE_REPOSITORY = new MovieRepositoryImpl();
    private static final MovieService MOVIE_SERVICE = new MovieServiceImpl(MOVIE_REPOSITORY);


    public static void openUserMenu() {
        while (true) {
            System.out.print("""
                                        
                    Cinema
                                        
                    1 - Buy ticket
                    2 - Show my tickets
                    3 - Return ticket
                    0 - Sign out
                                    
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> buyTicket();
                case "2" -> showTickets();
                case "3" -> returnTicket();
                case "0" -> {
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    private static void buyTicket() {
        System.out.println("\nChoose a movie to buy:\n");
        for (int i = 0; i < movieCatalog.size(); i++) {
            System.out.println((i + 1) + " - " + movieCatalog.get(i).getTitle());
        }
        System.out.print("\nEnter: ");
        String preVal = SCANNER.nextLine();
        if (preVal.length() == 1) {
            if (!(preVal.charAt(0) + "").matches("[1-9]")) {
                getErrorMessage();
                return;
            }
        } else if (preVal.length() == 2) {
            if (!(preVal.charAt(0) + "").matches("[1-9]") || !(preVal.charAt(1) + "").matches("0")) {
                getErrorMessage();
                return;
            }
        } else {
            getErrorMessage();
            return;
        }
        int val = Integer.parseInt(preVal) - 1;
        if (val > 9) {
            getErrorMessage();
            return;
        }
        String movTitle = movieCatalog.get(val).getTitle();
        boolean isThereTicket = false;
        for (cinema.model.Movie movie : movieCatalog) {
            if (movTitle.equals(movie.getTitle())) {
                List<Ticket> movieTickets = movie.getTicketList();
                boolean loopWorks = true;
                for (int j = 0; loopWorks && j < movieTickets.size(); j++) {
                    if (movieTickets.get(j).getIsNotBought()) {
                        movieTickets.get(j).setUser(user.getLogin());
                        movieTickets.get(j).setIsNotBought(false);
                        TICKET_SERVICE.addToTicketTable(movieTickets.get(j));
                        MOVIE_SERVICE.updateMovieTable(movie);
                        int id = TICKET_SERVICE.getIdFromTicketTable(movieTickets.get(j));
                        movieTickets.get(j).setId(id);
                        loopWorks = false;
                        isThereTicket = true;
                    }
                }
            }
        }
        if (isThereTicket)
            System.out.println("\nTicket for the movie «" + movTitle + "» successfully purchased.");
        else
            System.out.println("\nThere are no more tickets for the movie «" + movTitle + "»!");
    }

    private static void showTickets() {
        System.out.println("\nMy tickets");
        List<Ticket> ticketList;
        for (cinema.model.Movie movie : movieCatalog) {
            ticketList = movie.getTicketList();
            for (Ticket ticket : ticketList) {
                if (ticket.getUser() != null && ticket.getUser().equals(user.getLogin())) {
                    System.out.println("\nTicket ID: " + ticket.getId() + ticket);
                }
            }
        }
    }

    private static void returnTicket() {
        System.out.println("\nChoose a ticket ID to return:");
        showTickets();
        System.out.print("\nEnter: ");
        String preVal = SCANNER.nextLine();
        for (cinema.model.Movie movie : movieCatalog) {
            List<Ticket> movieTickets = movie.getTicketList();
            boolean loopWorks = true;
            for (int j = 0; loopWorks && j < movieTickets.size(); j++) {
                if (movieTickets.get(j).getId() == Integer.parseInt(preVal)) {
                    movieTickets.get(j).setUser(null);
                    movieTickets.get(j).setIsNotBought(true);
                    TICKET_SERVICE.removeFromTicketTable(movieTickets.get(j));
                    movieTickets.get(j).setId(0);
                    MOVIE_SERVICE.updateMovieTable(movie);
                    loopWorks = false;
                }
            }
        }
        System.out.println("Ticket returned");
    }
}
