package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TenmoService {
    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private String AUTH_TOKEN;

    public TenmoService(String BASE_URL, AuthenticatedUser currentUser) {
        this.BASE_URL = BASE_URL;
        this.currentUser = currentUser;
    }

    public BalanceData getBalance() {
        BalanceData balanceObject = new BalanceData();
        try {
            balanceObject = restTemplate.exchange(BASE_URL + "/get-balance", HttpMethod.GET, makeAuthEntity(), BalanceData.class).getBody();

        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return balanceObject;
    }

    public User[] getAllUsers() {
        try {
            return restTemplate.exchange(BASE_URL + "/see-all-userid", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void sendMoney(int id, BigDecimal amount) {

        Transfers transfers = new Transfers();
        transfers.setAccountFrom(currentUser.getUser().getId());
        transfers.setAccountTo(id);
        transfers.setAmount(amount);

        try {
            restTemplate.exchange(BASE_URL + "/transfer/", HttpMethod.POST, makeTransferEntity(transfers), Transfers.class).getBody();
            System.out.println("Your transfer was successful.");

        } catch (
                RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (
                ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void requestMoney() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter ID of the user you are requesting from (0 to cancel): ");
        String userId = scanner.nextLine();
        int intUserId = Integer.parseInt(userId);
        System.out.println("Enter amount: ");
        String amount = scanner.nextLine();
        BigDecimal bigDecimalAmount = BigDecimal.valueOf(Long.parseLong(amount));

        Transfers transfers = new Transfers();
        transfers.setAccountFrom(currentUser.getUser().getId());
        transfers.setAccountTo(intUserId);
        transfers.setAmount(bigDecimalAmount);

        try {
            restTemplate.exchange(BASE_URL + "/request-money/" + intUserId, HttpMethod.POST, makeTransferEntity(transfers), Transfers.class).getBody();
            System.out.println("Your request is pending.");

        } catch (
                RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (
                ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void viewTransfers() {
        try {
            Transfers[] transfersArr = restTemplate.exchange(BASE_URL + "/see-all-transfers/" + currentUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), Transfers[].class).getBody();
            for (Transfers transfers : transfersArr) {
                System.out.printf("%-20s %-10s\n", transfers.getTransferId(), transfers.getAmount());
            }
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void viewTransferDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose transfer IDs from above to see the transfer details");
        String transferId = scanner.nextLine();
        int transferIdInt = Integer.parseInt(transferId);
        try {
            TransferDetails[] transferDetails = restTemplate.exchange(BASE_URL + "/see-transfer-details/" + transferIdInt, HttpMethod.GET, makeAuthEntity(), TransferDetails[].class).getBody();
            for (TransferDetails details : transferDetails) {
                if (transferIdInt == details.getTransferId()) {
                    System.out.println(details);
                }
            }
        } catch (RestClientResponseException ex) {
            System.out.println((ex.getRawStatusCode() + " : " + ex.getStatusText()));
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private HttpEntity<Transfers> makeTransferEntity(Transfers transfer) {
        String userToken = currentUser.getToken();
        AUTH_TOKEN = userToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<Transfers> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthEntity() {
        String userToken = currentUser.getToken();
        AUTH_TOKEN = userToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
