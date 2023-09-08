package cinema.controller.helper;

import cinema.exeptions.IDNotFoundException;
import cinema.model.Movie;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.repository.MovieRepository;
import cinema.repository.MovieRepositoryImpl;
import cinema.repository.TicketRepository;
import cinema.repository.TicketRepositoryImpl;
import cinema.service.MovieService;
import cinema.service.MovieServiceImpl;
import cinema.service.TicketService;
import cinema.service.TicketServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static cinema.controller.GlobalController.*;
import static cinema.util.DateGenerator.getDate;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.InputValidation.checkInput;
import static cinema.util.ScannerUtil.SCANNER;

@Slf4j
public class AllRoleController {

    private static final TicketRepository TICKET_REPOSITORY = new TicketRepositoryImpl();
    private static final TicketService TICKET_SERVICE = new TicketServiceImpl(TICKET_REPOSITORY);
    private static final MovieRepository MOVIE_REPOSITORY = new MovieRepositoryImpl();
    private static final MovieService MOVIE_SERVICE = new MovieServiceImpl(MOVIE_REPOSITORY);
    private static String userName;

    public static void buyTicket() {
        while (true) {
            System.out.println("\nChoose a movie to buy a ticket:\n");
            for (int i = 0; i < movieCatalog.size(); i++) {
                System.out.println((i + 1) + " - " + movieCatalog.get(i).getTitle());
            }
            System.out.print("\n0 - Back\n\nEnter: ");
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
                        if (movieTickets.get(j).isTicketBought()) {
                            if (userName == null) {
                                movieTickets.get(j).setUser(user.getLogin());
                            } else {
                                movieTickets.get(j).setUser(userName);
                            }
                            movieTickets.get(j).setTicketBought(false);
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
            if (isThereTicket) {
                log.info("\"{}\" bought a movie ticket.", user.getLogin());
                System.out.println("\nTicket for the movie «" + movTitle + "» successfully purchased.");
            } else
                System.out.println("\nThere are no more tickets for the movie «" + movTitle + "»!");
        }
    }

    public static void showTickets() {
        System.out.println("\nTicket list");
        List<Ticket> ticketList;
        for (cinema.model.Movie movie : movieCatalog) {
            ticketList = movie.getTicketList();
            if (userName == null) {
                for (Ticket ticket : ticketList) {
                    if (ticket.getUser() != null && ticket.getUser().equals(user.getLogin())) {
                        System.out.println("\nTicket ID: " + ticket.getId() + ticket);
                    }
                }
            } else {
                for (Ticket ticket : ticketList) {
                    if (ticket.getUser() != null && ticket.getUser().equals(userName)) {
                        System.out.println("\nTicket ID: " + ticket.getId() + ticket);
                    }
                }
            }

        }
    }

    public static void returnTicket() {
        while (true) {
            System.out.println("\nChoose a ticket ID to return:");
            showTickets();
            System.out.print("\n0 - Back\n\nEnter: ");
            String val = SCANNER.nextLine();
            if (checkInput(val)) {
                continue;
            }
            if (val.equals("0"))
                return;
            boolean ticketReturned = false;
            for (cinema.model.Movie movie : movieCatalog) {
                List<Ticket> movieTickets = movie.getTicketList();
                boolean loopWorks = true;
                for (int j = 0; loopWorks && j < movieTickets.size(); j++) {
                    if (movieTickets.get(j).getId() == Integer.parseInt(val)) {
                        movieTickets.get(j).setUser(null);
                        movieTickets.get(j).setTicketBought(true);
                        ticketReturned = TICKET_SERVICE.removeFromTicketTable(movieTickets.get(j));
                        movieTickets.get(j).setId(0);
                        MOVIE_SERVICE.updateMovieTable(movie);
                        loopWorks = false;
                    }
                }
            }
            if (ticketReturned) {
                log.info("\"{}\" returned a movie ticket.", user.getLogin());
                System.out.println("\nTicket with ID: " + val + " returned.");
            } else
                System.out.println("\nTicket with ID: " + val + " does not exist!");
        }
    }

    private static void addMovie() {
        System.out.print("\nEnter movie name to add\n\nEnter: ");
        String title = SCANNER.nextLine();
        Movie movie = new Movie(title, getDate());
        MOVIE_SERVICE.addToMovieTable(movie);
        movieCatalog.add(movie);
        log.info("\"{}\" added a movie.", user.getLogin());
        System.out.println("\nThe movie «" + title + "» was added.");
    }

    private static void removeMovie() {
        System.out.print("\nEnter movie name to remove\n\nEnter: ");
        String title = SCANNER.nextLine();
        for (int i = 0; i < movieCatalog.size(); i++) {
            Movie movie = movieCatalog.get(i);
            if (movie.getTitle().equals(title)) {
                MOVIE_SERVICE.removeFromMovieTable(movie.getTitle());
                movieCatalog.remove(i);
                log.info("\"{}\" removed a movie.", user.getLogin());
                System.out.println("\nThe movie «" + title + "» has been removed.");
                return;
            }
        }
        System.out.println("\nThe movie «" + title + "» does not exist!");
    }

    private static void changeMovieDate() {
        System.out.print("\nEnter movie name to change date\n\nEnter: ");
        String title = SCANNER.nextLine();
        for (Movie movie : movieCatalog) {
            if (movie.getTitle().equals(title)) {
                System.out.print("\nEnter the date in the format «yyyy-MM-dd HH:mm:ss»\n\nEnter: ");
                String dateStr = SCANNER.nextLine();
                LocalDateTime date;
                try {
                    date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                } catch (DateTimeParseException e) {
                    getErrorMessage();
                    return;
                }
                movie.setDate(date);
                MOVIE_SERVICE.updateMovieTable(movie);
                log.info("\"{}\" changed a date of a movie.", user.getLogin());
                System.out.println("\nThe date of the movie «" + title + "» has been changed.");
                return;
            }
        }
        System.out.println("\nThe movie «" + title + "» does not exist!");
    }

    private static void changeMoviePrice() {
        System.out.print("\nEnter movie name to change price\n\nEnter: ");
        String title = SCANNER.nextLine();
        for (Movie movie : movieCatalog) {
            if (movie.getTitle().equals(title)) {
                System.out.print("\nEnter a price: ");
                String price = SCANNER.nextLine();
                for (int i = 0, count = 0; i < price.length(); i++) {
                    if ((price.charAt(i) == '.')) {
                        count++;
                        if (count == 2) {
                            getErrorMessage();
                            return;
                        }
                        continue;
                    }
                    if (!(price.charAt(i) + "").matches("[0-9]")) {
                        getErrorMessage();
                        return;
                    }
                }
                if (price.equals(".")) {
                    getErrorMessage();
                    return;
                }
                movie.setTicketPrice(Double.parseDouble(price));
                MOVIE_SERVICE.updateMovieTable(movie);
                log.info("\"{}\" changed a price of a movie.", user.getLogin());
                System.out.println("\nThe price of the movie «" + title + "» has been changed.");
                return;
            }
        }
        System.out.println("\nThe movie «" + title + "» does not exist!");
    }

    public static void editMovies() {
        while (true) {
            System.out.println("\nMovie editor\n");
            System.out.print("""
                    1 - Change date
                    2 - Change price
                    0 - Back
                                        
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> changeMovieDate();
                case "2" -> changeMoviePrice();
                case "0" -> {
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    public static void editMovieList() {
        while (true) {
            System.out.println("\nMovie list editor\n");
            System.out.print("""
                    1 - Change date
                    2 - Change price
                    3 - Add a movie
                    4 - Remove a movie
                    0 - Back
                                        
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> changeMovieDate();
                case "2" -> changeMoviePrice();
                case "3" -> addMovie();
                case "4" -> removeMovie();
                case "0" -> {
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    public static void buyTicketToUser() {
        User returnTicketUser = getUserById();
        if (returnTicketUser != null) {
            userName = returnTicketUser.getLogin();
            buyTicket();
            userName = null;
        }
    }

    public static void returnUserTicket() {
        User returnTicketUser = getUserById();
        if (returnTicketUser != null) {
            userName = returnTicketUser.getLogin();
            returnTicket();
            userName = null;
        }
    }

    public static void createUser() {
        System.out.print("\nNew user registration\n\nEnter login: ");
        String username = SCANNER.nextLine();
        System.out.print("Enter password: ");
        String password = SCANNER.nextLine();
        User newUser = new User(username, password);
        if (USER_SERVICE.signUp(newUser)) {
            log.info("\"{}\" has registered a new user.", user.getLogin());
            System.out.println("\nUser registered.");
        }
    }

    public static void deleteUser() {
        User delUser = getUserById();
        if (delUser != null && USER_SERVICE.deleteAccount(delUser.getId())) {
            log.info("\"{}\" has deleted a user.", user.getLogin());
            System.out.println("\nUser deleted.");
        }
    }

    public static void getAllUsers() {
        System.out.println();
        USER_SERVICE.getAllUsers().forEach(System.out::println);
    }

    private static User getUserById() {
        System.out.print("\nEnter user id: ");
        String val = SCANNER.nextLine();
        if (checkInput(val)) {
            return null;
        }
        int usrId = Integer.parseInt(val);
        try {
            return USER_SERVICE.getUserById(usrId)
                    .orElseThrow(() -> new IDNotFoundException(
                            String.format("User ID %d not found!", usrId)));
        } catch (IDNotFoundException e) {
            System.out.println();
            System.out.println(e.getMessage());
        }
        return null;
    }
}
