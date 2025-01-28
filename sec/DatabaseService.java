package sec;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.owasp.encoder.Encode;

public class DatabaseService {
    private static final Logger logger = Logger.getLogger(DatabaseService.class.getName());

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/atmdb", "root", "");
        } catch (Exception e) {
            logger.log(Level.SEVERE, Encode.forJava("[DEBUG] Database connection failed: "), e);
            System.out.println(Encode.forJava("Unable to connect to database. Please try again later."));
            return null;
        }
    }
}