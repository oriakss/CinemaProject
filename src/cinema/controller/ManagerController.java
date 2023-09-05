package cinema.controller;

import static cinema.controller.AllRoleController.*;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.ScannerUtil.SCANNER;

public class ManagerController {

    public static void openManagerMenu() {
        while (true) {
            System.out.print("""
                                        
                    Cinema
                                        
                    1 - Buy ticket
                    2 - Show my tickets
                    3 - Return ticket
                    4 - Edit movie list
                    5 - Buy a ticket to a user
                    6 - Return user ticket
                    0 - Sign out
                                    
                    Enter:\s""");
            switch (SCANNER.nextLine()) {
                case "1" -> buyTicket();
                case "2" -> showTickets();
                case "3" -> returnTicket();
                case "4" -> editMovieList();
                case "5" -> buyTicketToUser();
                case "6" -> returnUserTicket();
                case "0" -> {
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }
}
