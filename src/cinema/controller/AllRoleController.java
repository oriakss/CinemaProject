package cinema.controller;

import cinema.model.Movie;
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
import static cinema.util.DataGenerator.getDate;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.InputValidation.checkInput;
import static cinema.util.ScannerUtil.SCANNER;

public class AllRoleController {

    private static final TicketRepository TICKET_REPOSITORY = new TicketRepositoryImpl();
    private static final TicketService TICKET_SERVICE = new TicketServiceImpl(TICKET_REPOSITORY);
    private static final MovieRepository MOVIE_REPOSITORY = new MovieRepositoryImpl();
    private static final MovieService MOVIE_SERVICE = new MovieServiceImpl(MOVIE_REPOSITORY);

    static void buyTicket() {
        while (true) {
            System.out.println("\nChoose a movie to buy a ticket:\n");
            for (int i = 0; i < movieCatalog.size(); i++) {
                System.out.println((i + 1) + " - " + movieCatalog.get(i).getTitle());
            }
            System.out.println("\n0 - Back");
            System.out.print("\nEnter: ");
            String preVal = SCANNER.nextLine();
            if (checkInput(preVal)) {
                continue;
            }
            if (preVal.equals("0"))
                return;
            int val = Integer.parseInt(preVal);
            if (movieCatalog.size() < val) {
                getErrorMessage();
                continue;
            }
            val--;
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
    }

    static void showTickets() {
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

    static void returnTicket() {
        while (true) {
            System.out.println("\nChoose a ticket ID to return:");
            showTickets();
            System.out.println("\n0 - Back");
            System.out.print("\nEnter: ");
            String preVal = SCANNER.nextLine();
            if (checkInput(preVal)) {
                continue;
            }
            if (preVal.equals("0"))
                return;
            boolean ticketReturned = false;
            for (cinema.model.Movie movie : movieCatalog) {
                List<Ticket> movieTickets = movie.getTicketList();
                boolean loopWorks = true;
                for (int j = 0; loopWorks && j < movieTickets.size(); j++) {
                    if (movieTickets.get(j).getId() == Integer.parseInt(preVal)) {
                        movieTickets.get(j).setUser(null);
                        movieTickets.get(j).setIsNotBought(true);
                        ticketReturned = TICKET_SERVICE.removeFromTicketTable(movieTickets.get(j));
                        movieTickets.get(j).setId(0);
                        MOVIE_SERVICE.updateMovieTable(movie);
                        loopWorks = false;
                    }
                }
            }
            if (ticketReturned)
                System.out.println("\nTicket returned");
        }
    }

    private static void addMovie() {
        System.out.println("\nEnter movie name to add");
        System.out.print("\nEnter: ");
        String title = SCANNER.nextLine();
        Movie movie = new Movie(title, getDate());
        MOVIE_SERVICE.addToMovieTable(movie);
        movieCatalog.add(movie);
        System.out.println("\nMovie «" + title + "» was added");
    }

    private static void removeMovie() {
        System.out.println("\nEnter movie name to remove");
        System.out.print("\nEnter: ");
        String title = SCANNER.nextLine();
        for (int i = 0; i < movieCatalog.size(); i++) {
            Movie movie = movieCatalog.get(i);
            if (movie.getTitle().equals(title)) {
                MOVIE_SERVICE.removeFromMovieTable(movie.getTitle());
                movieCatalog.remove(i);
                System.out.println("\nMovie «" + title + "» was removed");
                return;
            }
        }
        System.out.println("\nMovie «" + title + "» does not exist!");
    }

    static void editMovieList() {
        while (true) {
            System.out.println("\nMovie list editor\n");
            System.out.println("""
                    1 - Add a movie
                    2 - Remove a movie
                    0 - Back""");
            System.out.print("\nEnter: ");
            switch (SCANNER.nextLine()) {
                case "1" -> addMovie();
                case "2" -> removeMovie();
                case "0" -> {
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    static void buyTicketToUser() {

    }

    static void returnUserTicket() {

    }

    static void createUser() {

    }

    static void deleteUser() {

    }
}
