package sec;

import java.util.Scanner;
import java.util.logging.Logger;
import org.owasp.encoder.Encode;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        logger.info(Encode.forJava("[DEBUG] Starting ATM System..."));
        ATM atm = new ATM();
        atm.run();
        logger.info(Encode.forJava("[DEBUG] ATM System terminated."));
        scanner.close();
    }
}