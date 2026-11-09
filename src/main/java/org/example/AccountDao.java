package org.example;

// Import the Account model.

// Import the database connection utility.

// Import BigDecimal for monetary values.
import java.math.BigDecimal;

// Import JDBC classes.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

// Import collection classes.
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao {

    // CREATE: Insert a new account into PostgreSQL.
    public Account createAccount(
            String accountNumber,
            String accountName,
            BigDecimal balance
    ) throws SQLException {

        // SQL statement for inserting an account.
        String sql = """
                INSERT INTO accounts
                    (account_number, account_name, balance, status)
                VALUES
                    (?, ?, ?, ?)
                """;

        // Open a database connection.
        // Request the generated account ID.
        try (
                Connection connection = DatabaseConnection.getConnection();

                PreparedStatement ps = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            // Supply the account number.
            ps.setString(1, accountNumber);

            // Supply the account holder's name.
            ps.setString(2, accountName);

            // Supply the opening balance.
            ps.setBigDecimal(3, balance);

            // New accounts start as ACTIVE.
            ps.setString(4, "ACTIVE");

            // Execute the INSERT statement.
            int rowsInserted = ps.executeUpdate();

            // Ensure that a row was inserted.
            if (rowsInserted == 0) {
                throw new SQLException("Account creation failed.");
            }

            // Retrieve the generated primary key.
            try (ResultSet keys = ps.getGeneratedKeys()) {

                // Check whether PostgreSQL returned the generated ID.
                if (keys.next()) {

                    // Read the generated account ID.
                    long accountId = keys.getLong(1);

                    // Return the newly created Account object.
                    return new Account(
                            accountId,
                            accountNumber,
                            accountName,
                            balance,
                            "ACTIVE"
                    );
                }

                // Report an unexpected missing generated key.
                throw new SQLException(
                        "Account was inserted but its ID was not returned."
                );
            }
        }
    }

    // READ: Find one account using its account number.
    public Optional<Account> findByAccountNumber(
            String accountNumber
    ) throws SQLException {

        // SQL query for retrieving one account.
        String sql = """
                SELECT account_id,
                       account_number,
                       account_name,
                       balance,
                       status
                FROM accounts
                WHERE account_number = ?
                """;

        // Open a connection and prepare the query.
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            // Supply the account number.
            ps.setString(1, accountNumber);

            // Execute the SELECT query.
            try (ResultSet rs = ps.executeQuery()) {

                // Check whether a matching row exists.
                if (rs.next()) {

                    // Convert the database row into an Account object.
                    Account account = mapRowToAccount(rs);

                    // Return the account wrapped in Optional.
                    return Optional.of(account);
                }

                // Return an empty Optional when no account exists.
                return Optional.empty();
            }
        }
    }

    // READ: Retrieve all accounts from PostgreSQL.
    public List<Account> findAllAccounts() throws SQLException {

        // SQL query for retrieving all accounts.
        String sql = """
                SELECT account_id,
                       account_number,
                       account_name,
                       balance,
                       status
                FROM accounts
                ORDER BY account_id
                """;

        // Create a collection to store account objects.
        List<Account> accounts = new ArrayList<>();

        // Open a connection and prepare the query.
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            // Process every returned database row.
            while (rs.next()) {

                // Convert the current row into an Account object.
                Account account = mapRowToAccount(rs);

                // Add the account to the collection.
                accounts.add(account);
            }
        }

        // Return the collection of retrieved accounts.
        return accounts;
    }

    // UPDATE: Change the name associated with an account.
    public boolean updateAccountName(
            String accountNumber,
            String newAccountName
    ) throws SQLException {

        // SQL statement for updating an account name.
        String sql = """
                UPDATE accounts
                SET account_name = ?
                WHERE account_number = ?
                """;

        // Open a connection and prepare the UPDATE statement.
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            // Set the new account name.
            ps.setString(1, newAccountName);

            // Identify the account to update.
            ps.setString(2, accountNumber);

            // Execute the UPDATE operation.
            int rowsUpdated = ps.executeUpdate();

            // Return true if an account was updated.
            return rowsUpdated > 0;
        }
    }

    // DELETE: Remove an account using its account number.
    public boolean deleteAccount(String accountNumber)
            throws SQLException {

        // SQL statement for deleting a specific account.
        String sql = """
                DELETE FROM accounts
                WHERE account_number = ?
                """;

        // Open a connection and prepare the DELETE statement.
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            // Identify the account to delete.
            ps.setString(1, accountNumber);

            // Execute the DELETE operation.
            int rowsDeleted = ps.executeUpdate();

            // Return true if a record was deleted.
            return rowsDeleted > 0;
        }
    }

    // Convert one ResultSet row into an Account object.
    private Account mapRowToAccount(ResultSet rs) throws SQLException {

        // Read the generated account ID.
        long accountId = rs.getLong("account_id");

        // Read the account number.
        String accountNumber = rs.getString("account_number");

        // Read the account holder's name.
        String accountName = rs.getString("account_name");

        // Read the account balance.
        BigDecimal balance = rs.getBigDecimal("balance");

        // Read the account status.
        String status = rs.getString("status");

        // Return the mapped Java object.
        return new Account(
                accountId,
                accountNumber,
                accountName,
                balance,
                status
        );
    }
}
