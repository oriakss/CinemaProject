package cinema.controller;

import cinema.model.Movie;
import cinema.model.MovieList;
import cinema.model.User;
import cinema.repository.MovieRepository;
import cinema.repository.MovieRepositoryImpl;
import cinema.repository.UserRepository;
import cinema.repository.UserRepositoryImpl;
import cinema.service.MovieService;
import cinema.service.MovieServiceImpl;
import cinema.service.UserService;
import cinema.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static cinema.controller.UserController.openUserMenu;
import static cinema.util.DataGenerator.getDate;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.ScannerUtil.SCANNER;

public class GlobalController {

    private static final UserRepository USER_REPOSITORY = new UserRepositoryImpl();
    private static final UserService USER_SERVICE = new UserServiceImpl(USER_REPOSITORY);
    private static final MovieRepository MOVIE_REPOSITORY = new MovieRepositoryImpl();
    private static final MovieService MOVIE_SERVICE = new MovieServiceImpl(MOVIE_REPOSITORY);
    public static User user;
    public static List<Movie> movieCatalog;

    public static void start() {
        prepareMovieCatalog();
        System.out.println("Welcome to the cinema!");
        while (true) {
            System.out.print("""
                                        
                    1 - Sign in
                    2 - Register
                    0 - Exit the cinema
                    3 - getAll(temp)
                    4 - getUserById(temp)
                                  
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> singIn();
                case "2" -> signUp();
                case "3" -> getAll();
                case "4" -> getUserById();
                case "0" -> {
                    System.out.println("\nGoodbye!");
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    private static void prepareMovieCatalog() {
        if (MOVIE_SERVICE.checkIsEmptyMovieTable()) {
            movieCatalog = new ArrayList<>();
            Movie movie;
            for (int i = 0; i < MovieList.values().length; i++) {
                movie = new Movie(MovieList.values()[i] + "", getDate());
                movieCatalog.add(movie);
                MOVIE_SERVICE.addToMovieTable(movie);
            }
        } else {
            movieCatalog = MOVIE_SERVICE.getMovieTable();
        }

    }

    private static void signUp() {
        System.out.print("\nRegistration\n\nEnter login: ");
        String username = SCANNER.nextLine();
        System.out.print("Enter password: ");
        String password = SCANNER.nextLine();
        user = new User(username, password);
        USER_SERVICE.signUp(user);
        openUserMenu();
    }

    private static void singIn() {
        System.out.print("\nSign in\n\nEnter login: ");
        String username = SCANNER.nextLine();
        System.out.print("Enter password: ");
        String password = SCANNER.nextLine();
        user = new User(username, password);
        if (USER_SERVICE.signIn(user)) {
            openUserMenu();
        }
    }

    private static void getAll() {
        USER_SERVICE.getAll();
    }

    private static void getUserById() {
        System.out.println("Enter user id: ");
        int usrId = Integer.parseInt(SCANNER.nextLine());
        User user = USER_SERVICE.getUserById(usrId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("User ID %d not found!", usrId)));
        System.out.println(user);
    }
}