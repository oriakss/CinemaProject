package cinema.controller;

import cinema.model.Movie;
import cinema.model.MovieList;
import cinema.model.User;
import cinema.model.UserRole;
import cinema.repository.MovieRepository;
import cinema.repository.MovieRepositoryImpl;
import cinema.repository.UserRepository;
import cinema.repository.UserRepositoryImpl;
import cinema.service.MovieService;
import cinema.service.MovieServiceImpl;
import cinema.service.UserService;
import cinema.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static cinema.controller.AdminController.openAdminMenu;
import static cinema.controller.ManagerController.openManagerMenu;
import static cinema.controller.UserController.openUserMenu;
import static cinema.util.DateGenerator.getDate;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.ScannerUtil.SCANNER;

@Slf4j
public class GlobalController {

    private static final UserRepository USER_REPOSITORY = new UserRepositoryImpl();
    public static final UserService USER_SERVICE = new UserServiceImpl(USER_REPOSITORY);
    private static final MovieRepository MOVIE_REPOSITORY = new MovieRepositoryImpl();
    private static final MovieService MOVIE_SERVICE = new MovieServiceImpl(MOVIE_REPOSITORY);
    public static User user;
    public static List<Movie> movieCatalog;

    public static void start() {
        createAdminAndManager();
        prepareMovieCatalog();
        log.info("Application launched.");
        System.out.println("Welcome to the cinema!");
        while (true) {
            System.out.print("""
                                        
                    1 - Sign in
                    2 - Register
                    0 - Exit the cinema
                                  
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> singIn();
                case "2" -> signUp();
                case "0" -> {
                    System.out.println("\nGoodbye!");
                    log.info("Application completed successfully.");
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }

    private static void createAdminAndManager() {
        if (USER_SERVICE.IsNotExistAdminAndManager()) {
            User admin = new User();
            admin.setLogin("admin");
            admin.setPassword("admin");
            admin.setRole(UserRole.ADMIN);
            User manager = new User();
            manager.setLogin("manager");
            manager.setPassword("manager");
            manager.setRole(UserRole.MANAGER);
            USER_SERVICE.createAdminAndManager(admin, manager);
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
        if (!USER_SERVICE.signUp(user))
            return;
        log.info("User \"{}\" has registered.", user.getLogin());
        openUserMenu();
    }

    private static void singIn() {
        System.out.print("\nSign in\n\nEnter login: ");
        String username = SCANNER.nextLine();
        System.out.print("Enter password: ");
        String password = SCANNER.nextLine();
        user = new User(username, password);
        if (username.equals("admin")) {
            user.setRole(UserRole.ADMIN);
        } else if (username.equals("manager")) {
            user.setRole(UserRole.MANAGER);
        }
        if (USER_SERVICE.signIn(user)) {
            log.info("User \"{}\" is logged in.", user.getLogin());
            switch (user.getRole()) {
                case USER -> openUserMenu();
                case MANAGER -> openManagerMenu();
                case ADMIN -> openAdminMenu();
            }
        }
    }
}