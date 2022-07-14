package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.*;
import io.cucumber.java.en_old.Ac;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService = null;
    private TransferService transferService = null;

    private UserService userService = null;

    private AuthenticatedUser currentUser;
    private User user;
    private Account account;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        accountService = new AccountService(API_BASE_URL, currentUser);
        transferService = new TransferService(API_BASE_URL, currentUser);
        userService = new UserService(API_BASE_URL, currentUser);
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
//        currentUser.getToken();
        BigDecimal balance = accountService.getBalance(currentUser);
        System.out.println("Your current account balance is: $" + balance);


    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        TransferDetails[] transferDetailsArray = transferService.getTransfersWithDetails();

        System.out.println("----------------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-20s%-20s%-20s", "ID", "From/To", "Amount");
        System.out.println("");
        System.out.println("----------------------------------------------------");
        for (TransferDetails transferDetails : transferService.getTransfersWithDetails()) {
            boolean isTransfer = transferDetails.getUsername().equals(currentUser.getUser().getUsername());
            int id = transferDetails.getTransferId();
            String label = isTransfer ? "To:" : "From:";
            int userId = isTransfer ? transferDetails.getAccountTo() : transferDetails.getAccountFrom();
            String name = userService.getUsername(userId);
            BigDecimal amount = transferDetails.getAmount();
            System.out.printf("%-20s%-6s%-14s%-20s\n", id, label, name, amount);
        }


        int userSelection = consoleService.promptForInt("Input a Transfer ID to view Transfer Details: ");
        System.out.println("----------------------------------------------------");

        for (TransferDetails transferDetails : transferDetailsArray) {
            if (transferDetails.getTransferId() == userSelection) {
                System.out.println("Id:    \t\t" + transferDetails.getTransferId() + "\n" +
                        "From:  \t\t" + userService.getUsername(transferDetails.getAccountFrom()) + "\n" +
                        "To:    \t\t" + userService.getUsername(transferDetails.getAccountTo()) + "\n" +
                        "Type:  \t\tSend \n" +
                        "Status:\t\tApproved \n" +
                        "Amount:\t\t$" + transferDetails.getAmount());
            }
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        // TODO Auto-generated method stub


        User[] users = accountService.getAllUsers();
        System.out.println("----------------------------------------------------" +
                "\nUser ID: |" + "\t User Name:");
        for (User user : users) {
            System.out.println(user.getId() + "\t |     " + user.getUsername());

        }
        System.out.println("----------------------------------------------------");
        int userSelection = consoleService.promptForInt("Please enter User Id to transfer to(0 to cancel): ");
        BigDecimal amountToTransfer = consoleService.promptForBigDecimal("Please enter amount to transfer: ");
        if (amountToTransfer.compareTo(BigDecimal.valueOf(0)) == -1 || amountToTransfer.compareTo(BigDecimal.valueOf(0)) == 0) {
            System.out.println("Cannot transfer negative amounts or 0, ya goof.");
            return;
        }

        Account accountFrom = accountService.getAccount(currentUser.getUser().getId());
        Account accountTo = accountService.getAccount(userSelection);
        if (accountTo.getUserId() == accountFrom.getUserId()) {
            System.out.println("You cannot transfer to yourself");

        } else {
            Transfer newTransfer = new Transfer(2, 2, accountFrom.getAccountId(), accountTo.getAccountId(), amountToTransfer);
            transferService.createTransfer(newTransfer);

            BigDecimal newBalanceAccountFrom = accountFrom.getBalance().subtract(amountToTransfer);
            BigDecimal newBalanceAccountTo = accountTo.getBalance().add(amountToTransfer);

            accountFrom.setBalance(newBalanceAccountFrom);
            accountTo.setBalance(newBalanceAccountTo);
            accountService.updateAccountAfterTransfer(accountFrom);
            accountService.updateAccountAfterTransfer(accountTo);
            System.out.println("Transfer successful \n" + "Your new balance is: " + accountFrom.getBalance() + "\n User ID:" + accountTo.getUserId() + " has received $" + amountToTransfer);
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}
