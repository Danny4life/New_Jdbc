package org.example;

// Import BigDecimal for money.
import java.math.BigDecimal;

// Import SQL exception handling.
import java.sql.SQLException;

// Import Optional for account lookup results.
import java.util.Optional;

// Import Scanner for user input.
import java.util.Scanner;

// Import List for retrieving all accounts.
import java.util.List;

public class Demo {

    // Main method: application execution starts here.
    public static void main(String[] args) {

        // Create the DAO object.
        AccountDao accountDao = new AccountDao();

        // Create Scanner to read console input.
        Scanner scanner = new Scanner(System.in);

        // Keep the menu running until the user exits.
        boolean running = true;

        // Continue while the application is running.
        while (running) {

            // Display the application menu.
            System.out.println("\n===== MYINVEST ACCOUNT MANAGEMENT =====");
            System.out.println("1. Create account");
            System.out.println("2. Find account");
            System.out.println("3. View all accounts");
            System.out.println("4. Update account name");
            System.out.println("5. Delete account");
            System.out.println("0. Exit");

            // Ask the user to select an operation.
            System.out.print("Choose an option: ");

            // Read the selected menu option.
            String choice = scanner.nextLine();

            // Execute the selected operation.
            try {

                // Check the user's choice.
                switch (choice) {

                    // CREATE operation.
                    case "1" -> {

                        // Ask for the account number.
                        System.out.print("Enter account number: ");
                        String number = scanner.nextLine();

                        // Ask for the account holder's name.
                        System.out.print("Enter account name: ");
                        String name = scanner.nextLine();

                        // Ask for the opening balance.
                        System.out.print("Enter opening balance: ");
                        BigDecimal balance =
                                new BigDecimal(scanner.nextLine());

                        // Create the account using the DAO.
                        Account account = accountDao.createAccount(
                                number,
                                name,
                                balance
                        );

                        // Display the newly created account.
                        System.out.println("Account created successfully.");
                        System.out.println(account);
                    }

                    // READ: Find one account.
                    case "2" -> {

                        // Ask for the account number.
                        System.out.print("Enter account number: ");
                        String number = scanner.nextLine();

                        // Retrieve the account from PostgreSQL.
                        Optional<Account> result =
                                accountDao.findByAccountNumber(number);

                        // Check whether an account was found.
                        if (result.isPresent()) {

                            // Display the retrieved account.
                            System.out.println(result.get());

                        } else {

                            // Inform the user that it does not exist.
                            System.out.println("Account not found.");
                        }
                    }

                    // READ: Retrieve all accounts.
                    case "3" -> {

                        // Retrieve all accounts.
                        List<Account> accounts =
                                accountDao.findAllAccounts();

                        // Check whether the database returned records.
                        if (accounts.isEmpty()) {

                            // Inform the user that no accounts exist.
                            System.out.println("No accounts found.");

                        } else {

                            // Display every account.
                            for (Account account : accounts) {
                                System.out.println(account);
                                System.out.println("-------------------");
                            }
                        }
                    }

                    // UPDATE operation.
                    case "4" -> {

                        // Ask which account should be updated.
                        System.out.print("Enter account number: ");
                        String number = scanner.nextLine();

                        // Ask for the new account name.
                        System.out.print("Enter new account name: ");
                        String newName = scanner.nextLine();

                        // Execute the account update.
                        boolean updated = accountDao.updateAccountName(
                                number,
                                newName
                        );

                        // Display the result.
                        if (updated) {
                            System.out.println("Account updated successfully.");
                        } else {
                            System.out.println("Account not found.");
                        }
                    }

                    // DELETE operation.
                    case "5" -> {

                        // Ask which account should be deleted.
                        System.out.print("Enter account number: ");
                        String number = scanner.nextLine();

                        // Ask for confirmation before deletion.
                        System.out.print("Confirm deletion? (yes/no): ");
                        String confirmation = scanner.nextLine();

                        // Delete only when the user confirms.
                        if (confirmation.equalsIgnoreCase("yes")) {

                            // Execute the DELETE operation.
                            boolean deleted =
                                    accountDao.deleteAccount(number);

                            // Display the result.
                            if (deleted) {
                                System.out.println(
                                        "Account deleted successfully."
                                );
                            } else {
                                System.out.println("Account not found.");
                            }

                        } else {

                            // Cancel the deletion.
                            System.out.println("Deletion cancelled.");
                        }
                    }

                    // EXIT operation.
                    case "0" -> {

                        // Stop the menu loop.
                        running = false;

                        // Inform the user.
                        System.out.println("Thank you for using MyInvest.");
                    }

                    // Handle invalid menu choices.
                    default -> System.out.println("Invalid option.");
                }

            } catch (NumberFormatException exception) {

                // Handle invalid monetary input.
                System.out.println(
                        "Invalid amount. Enter a valid decimal number."
                );

            } catch (SQLException exception) {

                // Handle database-related errors.
                System.err.println(
                        "Database operation failed: " + exception.getMessage()
                );
            }
        }

        // Close the Scanner when the application exits.
        scanner.close();
    }
}
