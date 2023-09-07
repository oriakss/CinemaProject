package cinema.controller;

import lombok.extern.slf4j.Slf4j;

import static cinema.controller.helper.AllRoleController.*;
import static cinema.controller.GlobalController.user;
import static cinema.util.InputErrorMessage.getErrorMessage;
import static cinema.util.ScannerUtil.SCANNER;

@Slf4j
public class UserController {

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
                    log.info("User \"{}\" is logged out", user.getLogin());
                    return;
                }
                default -> getErrorMessage();
            }
        }
    }
}
