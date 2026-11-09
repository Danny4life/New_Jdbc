package org.example;

// Import Connection, DriverManager and SQLException from java.sql.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionDemo {

    public static void main(String[] args) {

        // Step 1: Define the JDBC URL.
        // It identifies the PostgreSQL server and database.
        String url = System.getenv("DB_URL");


        // Step 2: Define the PostgreSQL username.
        // Replace this with your actual PostgreSQL username.
        String username = System.getenv("DB_USERNAME");

        // Step 3: Define the PostgreSQL password.
        // Replace this with your actual PostgreSQL password.
        String password = System.getenv("DB_PASSWORD");

        /*
         * Step 4: Declare a Connection reference.
         *
         * We initialize it to null because the connection
         * has not yet been established.
         */
        Connection connection = null;

        try {

            /*
             * Step 5: Ask DriverManager to establish a connection.
             *
             * DriverManager looks for a suitable registered driver.
             * The PostgreSQL driver handles database communication.
             *
             * If successful, a Connection object is returned.
             */
            connection = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            // Step 6: Check whether the connection is closed.
            if (!connection.isClosed()) {

                // If not closed, the connection is currently open.
                System.out.println("Database connection successful!");

            }

            // Step 7: Display the database connection information.
            System.out.println(
                    "Connected to: " +
                            connection.getMetaData().getURL()
            );

            // Step 8: Display the database username.
            System.out.println(
                    "Database user: " +
                            connection.getMetaData().getUserName()
            );

        } catch (SQLException exception) {

            /*
             * Step 9: Handle database connection errors.
             *
             * SQLException may occur if:
             * - PostgreSQL is not running.
             * - The URL is incorrect.
             * - The username or password is incorrect.
             * - The database does not exist.
             * - The driver cannot be found.
             */
            System.out.println("Database connection failed!");

            // Display the error message.
            System.out.println(
                    "Error: " + exception.getMessage()
            );

        } finally {

            /*
             * Step 10: Close the connection if it was established.
             *
             * The finally block executes whether the try block
             * succeeds or an exception occurs.
             */
            if (connection != null) {

                try {

                    // Release the database connection.
                    connection.close();

                    System.out.println(
                            "Database connection closed."
                    );

                } catch (SQLException exception) {

                    // Handle errors encountered while closing.
                    System.out.println(
                            "Error closing connection: " +
                                    exception.getMessage()
                    );
                }
            }
        }
    }
}

