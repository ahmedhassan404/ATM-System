package sec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.owasp.encoder.Encode;

public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class.getName());
    private static Scanner scanner = new Scanner(System.in);
    private DatabaseService dbService = new DatabaseService();

    public void signUp() {
        clearScreen();
        logger.info(Encode.forJava("[DEBUG] User registration started."));
        System.out.println(Encode.forJava("=== User Registration ==="));

        System.out.print(Encode.forJava("Enter username (max 20 chars): "));
        String username = safeInputString(20);

        System.out.print(Encode.forJava("Enter password (max 20 chars): "));
        String password = safeInputString(20);

        System.out.print(Encode.forJava("Enter starting balance: "));
        double balance = safeInputDouble();

        String role = "USER";

        if (!isValidUsername(username)) {
            System.out.println(Encode.forJava("Invalid username format. Please use alphanumeric characters only."));
            waitForEnter();
            return;
        }

        try {
            String hashedPassword = SecurityService.hashPassword(password);
            Connection connection = dbService.getConnection();

            if (connection != null) {
                String query = "INSERT INTO users (username, password, balance, role) VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setDouble(3, balance);
                preparedStatement.setString(4, role);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println(Encode.forJava("\nSign-up successful!"));
                } else {
                    System.out.println(Encode.forJava("\nSign-up failed."));
                }

                connection.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, Encode.forJava("[DEBUG] An exception occurred during sign-up: "), e);
            System.out.println(Encode.forJava("An error occurred during sign-up. Please try again."));
        }

        waitForEnter();
    }

    public void login() {
        clearScreen();
        logger.info(Encode.forJava("[DEBUG] User login started."));
        System.out.println(Encode.forJava("=== User Login ==="));

        System.out.print(Encode.forJava("Enter username (max 20 chars): "));
        String username = safeInputString(20);

        System.out.print(Encode.forJava("Enter password (max 20 chars): "));
        String password = safeInputString(20);

        try {
            String hashedPassword = SecurityService.hashPassword(password);
            Connection connection = dbService.getConnection();

            if (connection != null) {
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPassword);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println(Encode.forJava("\nLogin successful!"));
                    double balance = resultSet.getDouble("balance");
                    int userId = resultSet.getInt("id");
                    String role = resultSet.getString("role");

                    if (SecurityService.performMFA()) {
                        performATMOperations(connection, userId, balance, role);
                    } else {
                        System.out.println(Encode.forJava("MFA verification failed. Access denied."));
                        waitForEnter();
                    }
                } else {
                    System.out.println(Encode.forJava("\nInvalid username or password."));
                    waitForEnter();
                }

                connection.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, Encode.forJava("[DEBUG] An exception occurred during login: "), e);
            System.out.println(Encode.forJava("An error occurred during login. Please try again."));
            waitForEnter();
        }
    }

    private void performATMOperations(Connection connection, int userId, double balance, String role) {
        ATMOperations atmOperations = new ATMOperations(connection, userId, balance, role);
        atmOperations.run();
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[a-zA-Z0-9]{1,20}$");
    }

    private String safeInputString(int maxLength) {
        String input = scanner.nextLine().trim();
        if (input.length() > maxLength) {
            input = input.substring(0, maxLength);
            System.out.println(Encode.forJava("Input truncated to ") + Encode.forJava(String.valueOf(maxLength)) + Encode.forJava(" characters."));
        }
        return input;
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