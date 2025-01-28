package sec;

import java.util.Scanner;
import java.util.logging.Logger;
import org.owasp.encoder.Encode;

public class ATM {
    private static final Logger logger = Logger.getLogger(ATM.class.getName());
    private static Scanner scanner = new Scanner(System.in);
    private UserService userService = new UserService();

    public void run() {
        boolean running = true;

        while (running) {
            clearScreen();
            System.out.println(Encode.forJava("=== Welcome to the ATM System ==="));
            System.out.println(Encode.forJava("1. Sign Up"));
            System.out.println(Encode.forJava("2. Login"));
            System.out.println(Encode.forJava("3. Exit"));
            System.out.print(Encode.forJava("Enter your choice: "));

            int choice = safeInputInt();

            switch (choice) {
                case 1:
                    userService.signUp();
                    break;
                case 2:
                    userService.login();
                    break;
                case 3:
                    running = false;
                    System.out.println(Encode.forJava("Thank you for using our ATM. Goodbye!"));
                    break;
                default:
                    System.out.println(Encode.forJava("Invalid choice. Please try again."));
                    waitForEnter();
            }
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private int safeInputInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(Encode.forJava("Invalid input. Please enter a valid number: "));
            }
        }
    }

    private void waitForEnter() {
        System.out.println(Encode.forJava("\nPress Enter to continue..."));
        scanner.nextLine();
    }
}