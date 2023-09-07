package cinema.controller;

import lombok.extern.slf4j.Slf4j;

import static cinema.controller.helper.AllRoleController.*;
import static cinema.controller.GlobalController.user;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.ScannerUtil.SCANNER;

@Slf4j
public class AdminController {

    static void openAdminMenu() {
        while (true) {
            System.out.print("""
                                        
                    Cinema
                                        
                    1 - Buy ticket
                    2 - Show my tickets
                    3 - Return ticket
                    4 - Edit movie list
                    5 - Get user list
                    6 - Buy ticket to user
                    7 - Return user ticket
                    8 - Create user
                    9 - Delete user
                    0 - Sign out
                                    
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> buyTicket();
                case "2" -> showTickets();
                case "3" -> returnTicket();
                case "4" -> editMovieList();
                case "5" -> getAllUsers();
                case "6" -> buyTicketToUser();
                case "7" -> returnUserTicket();
                case "8" -> createUser();
                case "9" -> deleteUser();
                case "0" -> {
                    log.info("User \"{}\" is logged out", user.getLogin());
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }
}
