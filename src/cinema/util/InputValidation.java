package cinema.util;

import static cinema.util.InputErrorMessage.getErrorMessage;

public final class InputValidation {

    public static boolean checkInput(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!(input.charAt(i) + "").matches("[0-9]")) {
                getErrorMessage();
                return true;
            }
        }
        return false;
    }
}
