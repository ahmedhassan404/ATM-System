package sec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.owasp.encoder.Encode;

public class ATMOperations {
    private static final Logger logger = Logger.getLogger(ATMOperations.class.getName());
    private static Scanner scanner = new Scanner(System.in);
    private Connection connection;
    private int userId;
    private double balance;
    private String role;

    public ATMOperations(Connection connection, int userId, double balance, String role) {
        this.connection = connection;
        this.userId = userId;
        this.balance = balance;
        this.role = role;
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            clearScreen();
            System.out.println(Encode.forJava("=== ATM Operations ==="));
            System.out.println(Encode.forJava("Current Balance: $") + Encode.forJava(String.format("%.2f", balance)));
            System.out.println(Encode.forJava("\nPlease select an operation:"));
            System.out.println(Encode.forJava("1. Check Balance"));
            if ("ADMIN".equals(role)) {
                System.out.println(Encode.forJava("2. Admin Operation"));
            }
            System.out.println(Encode.forJava("3. Deposit Money"));
            System.out.println(Encode.forJava("4. Withdraw Money"));
            System.out.println(Encode.forJava("5. Exit"));
            System.out.print(Encode.forJava("\nEnter your choice: "));

            int choice = safeInputInt();

            switch (choice) {
                case 1:
                    System.out.println(Encode.forJava("\nCurrent Balance: $") + Encode.forJava(String.format("%.2f", balance)));
                    waitForEnter();
                    break;
                case 2:
                    if ("ADMIN".equals(role)) {
                        System.out.println(Encode.forJava("\nAdmin operation executed."));
                        waitForEnter();
                    } else {
                        System.out.println(Encode.forJava("\nAccess denied. You do not have admin privileges."));
                        waitForEnter();
                    }
                    break;
                case 3:
                    System.out.print(Encode.forJava("\nEnter deposit amount: $"));
                    double deposit = safeInputDouble();
                    if (deposit > 0) {
                        balance += deposit;
                        updateBalance(balance);
                        System.out.println(Encode.forJava("Deposit successful. New Balance: $") + Encode.forJava(String.format("%.2f", balance)));
                    } else {
                        System.out.println(Encode.forJava("Invalid deposit amount."));
                    }
                    waitForEnter();
                    break;
                case 4:
                    System.out.print(Encode.forJava("\nEnter withdrawal amount: $"));
                    double withdrawal = safeInputDouble();
                    if (withdrawal > 0) {
                        if (withdrawal <= balance) {
                            balance -= withdrawal;
                            updateBalance(balance);
                            System.out.println(Encode.forJava("Withdrawal successful. New Balance: $") + Encode.forJava(String.format("%.2f", balance)));
                        } else {
                            System.out.println(Encode.forJava("Insufficient balance."));
                        }
                    } else {
                        System.out.println(Encode.forJava("Invalid withdrawal amount."));
                    }
                    waitForEnter();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println(Encode.forJava("\nInvalid choice. Please select a valid option."));
                    waitForEnter();
            }
        }
    }

    private void updateBalance(double newBalance) {
        try {
            String query = "UPDATE users SET balance = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            logger.log(Level.SEVERE, Encode.forJava("[DEBUG] An exception occurred while updating balance: "), e);
            System.out.println(Encode.forJava("An error occurred while updating balance."));
        }
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

    private double safeInputDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(Encode.forJava("Invalid input. Please enter a valid amount: "));
            }
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void waitForEnter() {
        System.out.println(Encode.forJava("\nPress Enter to continue..."));
        scanner.nextLine();
    }
}