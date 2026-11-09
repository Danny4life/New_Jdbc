package org.example;

// Import JDBC Connection.
import java.sql.Connection;

// Import DriverManager to establish database connections.
import java.sql.DriverManager;

// Import SQLException for database-related errors.
import java.sql.SQLException;

public class DatabaseConnection {

    // Read the database URL from an environment variable.
    private static final String URL =
            System.getenv("DB_URL");

    // Read the database username from an environment variable.
    private static final String USERNAME =
            System.getenv("DB_USERNAME");

    // Read the database password from an environment variable.
    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    // Private constructor prevents unnecessary object creation.
    private DatabaseConnection() {
    }

    // Provide a reusable method for obtaining a database connection.
    public static Connection getConnection() throws SQLException {

        // Ensure that the required environment variables exist.
        if (URL == null || USERNAME == null || PASSWORD == null) {

            // Stop execution with a clear configuration error.
            throw new IllegalStateException(
                    "Database environment variables are not configured."
            );
        }

        // Establish and return a connection to PostgreSQL.
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
