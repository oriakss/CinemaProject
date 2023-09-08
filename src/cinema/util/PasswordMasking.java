package cinema.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PasswordMasking {

    public static String maskPassword() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            Thread hide = new PasswordMaskingThread();
            hide.start();
            String pass = in.readLine();
            hide.interrupt();
            return pass;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
