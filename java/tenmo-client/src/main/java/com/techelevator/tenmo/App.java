package com.techelevator.tenmo;

import com.techelevator.tenmo.models.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TenmoService;
import com.techelevator.view.ConsoleService;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;


public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private TenmoService tenmoService;

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
        this.console = console;
        this.authenticationService = authenticationService;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        this.tenmoService = new TenmoService(API_BASE_URL, currentUser);
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                exitProgram();
            }
        }
    }

    private void viewCurrentBalance() {
        BalanceData balanceObject = tenmoService.getBalance();
        System.out.println(balanceObject);
    }

    private void viewTransferHistory() {
        transferPrintHeader();
        tenmoService.viewTransfers();

        transferDetailsPrintHeader();
        tenmoService.viewTransferDetails();
    }

    private void viewPendingRequests() {
        System.out.println("Under construction!");
    }

    private void sendBucks() {
        System.out.println("Select from userIDs below.");

        printHeader();
        User[] users = tenmoService.getAllUsers();

        for (User user : users) {
            if (!user.getId().equals(currentUser.getUser().getId())) {
                System.out.printf("%-20s %-10s\n", user.getId(), user.getUsername());
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ID of the user you are sending to (0 to cancel): ");
        String userId = scanner.nextLine();
        int intUserId = Integer.parseInt(userId);

        System.out.println("Enter amount: ");
        String amount = scanner.nextLine();
        BigDecimal bigDecimalAmount = BigDecimal.valueOf(Long.parseLong(amount));

        BalanceData balanceData = new BalanceData();
        balanceData.setBalance(bigDecimalAmount);

        BalanceData currentUserBalanceData = new BalanceData();
        currentUserBalanceData.setBalance(tenmoService.getBalance().getBalance());

        if (currentUserBalanceData.getBalance().compareTo(bigDecimalAmount) != -1) {
            tenmoService.sendMoney(intUserId, bigDecimalAmount);
        } else {
            System.out.println("Unable to complete transfer due to insufficient funds.");
        }
    }

    private void requestBucks() {
        System.out.println("Select from userIDs below.");

            printHeader();
            User[] users = tenmoService.getAllUsers();

            for (User user : users) {
                if (!user.getId().equals(currentUser.getUser().getId())) {
                    System.out.printf("%-20s %-10s\n", user.getId(), user.getUsername());
                }
            }
            tenmoService.requestMoney();
    }

    private void exitProgram() {
        System.exit(0);
    }

    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered)
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }

    private void printHeader() {
        System.out.println("-------------------------------------------");
        System.out.println("User");
        System.out.printf("%-20s %-10s\n", "Id", "Name");
        System.out.println("-------------------------------------------");
    }

    private void transferPrintHeader() {
        System.out.println("-------------------------------------------");
        System.out.println("Transfer");
        System.out.printf("%-20s %-10s\n", "Id", "Amount");
        System.out.println("-------------------------------------------");
    }

    private void transferDetailsPrintHeader() {
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
    }
}
