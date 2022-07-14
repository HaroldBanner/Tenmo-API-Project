package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public TransferService(String baseUrl,AuthenticatedUser currentUser) {
        this.baseUrl = baseUrl;
        this.authToken = currentUser.getToken();
    }

    public boolean createTransfer(Transfer transfer) {
        boolean success = false;
        try {
            restTemplate.postForObject(API_BASE_URL + "transfer", makeTransferEntity(transfer), Transfer.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public Transfer[] getTransferList() {
        Transfer[] transferArray = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfer/",
                            HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferArray = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferArray;
    }

    public TransferDetails[] getTransfersWithDetails() {
        TransferDetails[] transferDetailsArray = null;
        try {
            ResponseEntity<TransferDetails[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfer/all",
                            HttpMethod.GET, makeAuthEntity(), TransferDetails[].class);
            transferDetailsArray = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferDetailsArray;
    }



    public Transfer[] getTransferListById(AuthenticatedUser currentUser) {
        Transfer[] transferArray = null;
        int id = currentUser.getUser().getId();
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfer/" + id,
                            HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            transferArray = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferArray;

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }
}
