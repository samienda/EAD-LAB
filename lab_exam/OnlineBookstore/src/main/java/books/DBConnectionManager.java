package books;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// SAMUEL ENDALE UGR/9314/14


/**
 * Manages the database connection lifecycle.
 */
@Getter
@Setter
@NoArgsConstructor
public class DBConnectionManager {
    private Connection connection;
    private String databaseUrl = "jdbc:mysql://localhost:3306/bookdb"; // Database URL
    private String dbUser = "root";                                   // Username for DB access
    private String dbPassword = "1234";                               // Password for DB access

    /**
     * Initiates the connection to the database.
     * 
     * @throws SQLException if an issue occurs while accessing the database
     * @throws ClassNotFoundException if the JDBC driver is unavailable
     */
    public void connect() throws SQLException, ClassNotFoundException {
        // Load the MySQL driver class
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish the connection
        connection = DriverManager.getConnection(databaseUrl, dbUser, dbPassword);
        System.out.println("Connection to the database was successful.");
    }

    /**
     * Terminates the connection to the database if it is active.
     * 
     * @throws SQLException if an issue occurs while closing the connection
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection to the database has been closed.");
        }
    }
}
