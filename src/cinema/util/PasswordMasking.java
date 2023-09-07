package cinema.util;

import java.io.*;

public class PasswordMasking implements Runnable{

    private static boolean end;

    public void run() {
        end = true;
        while (end) {
            System.out.print("\010*");
        }
    }

    public static String maskPassword() {
        PasswordMasking passMask = new PasswordMasking();
        Thread thread = new Thread(passMask);
        thread.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String password = reader.readLine();
            end = false;
            System.out.println(password);
            return password;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
