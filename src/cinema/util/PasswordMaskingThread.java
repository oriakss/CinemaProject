package cinema.util;

public class PasswordMaskingThread extends Thread {

    public void run() {
        while (!isInterrupted()) {
            System.out.print("\010*");
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    public PasswordMaskingThread() {
        super("Hiding passwords thread");
    }
}
