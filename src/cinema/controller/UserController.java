package cinema.controller;

import cinema.model.MovieList;
import cinema.model.Ticket;
import cinema.repository.MovieRepository;
import cinema.repository.MovieRepositoryImpl;
import cinema.repository.TicketRepository;
import cinema.repository.TicketRepositoryImpl;
import cinema.service.MovieService;
import cinema.service.MovieServiceImpl;
import cinema.service.TicketService;
import cinema.service.TicketServiceImpl;

import java.util.ArrayList;
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
    private static List<Ticket> purchasedTickets = new ArrayList<>();


    public static void openUserMenu() {
        while (true) {
            System.out.print("""
                                        
                    Cinema
                                        
                    1 - Buy ticket
                    2 - Return ticket
                    3 - Show my tickets
                    0 - Sign out
                                    
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> buyTicket();
                case "2" -> returnTicket();
                case "3" -> showTickets();
                case "0" -> {
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    private static void buyTicket() {
        System.out.println("\nChoose a movie:\n");
        for (int i = 0; i < MovieList.values().length; i++) {
            System.out.println((i + 1) + " - " + MovieList.values()[i]);
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
        String movTitle = String.valueOf(MovieList.values()[val]);
        for (cinema.model.Movie movie : movieCatalog) {
            if (movTitle.equals(movie.getTitle())) {
                List<Ticket> movieTickets = movie.getTicketList();
                boolean flag = true;
                for (int j = 0; flag && j < movieTickets.size(); j++) {
                    if (movieTickets.get(j).getIsNotBought()) {
                        movieTickets.get(j).setUser(user);
                        movieTickets.get(j).setIsNotBought(false);
                        purchasedTickets.add(movieTickets.get(j));
                        TICKET_SERVICE.addToDB(movieTickets.get(j));
                        flag = false;
                    }
                }
                MOVIE_SERVICE.updateInDB(movie);
            }
        }
        System.out.println("\nTicket for the movie «" + movTitle + "» successfully purchased.");
    }

    private static void returnTicket() {


        System.out.println("Ticket returned");
    }

    private static void showTickets() {

    }
}
