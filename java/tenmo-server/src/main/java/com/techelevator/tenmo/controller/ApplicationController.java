package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransfersDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.BalanceData;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class ApplicationController {

    @Autowired
    AccountDAO accountDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TransfersDAO transfersDAO;

    @RequestMapping (path = "/get-balance", method = RequestMethod.GET)
    public BalanceData processBalanceRequests(Principal principal) {
        System.out.println("Requesting Balance");
        System.out.println(principal.getName());

        int correspondingUserId = userDAO.findIdByUsername(principal.getName());
        BalanceData balanceObject = accountDAO.getBalanceGivenAnId(correspondingUserId);
        System.out.println(balanceObject.getBalance());

        return balanceObject;
    }

    @RequestMapping(path = "/see-all-userid", method = RequestMethod.GET)
    public List<User> seeAllUserIds(Principal principal) {

       List<User> allUsers = userDAO.findAll();
       return allUsers;
    }

    //id of the person we are requesting money from
    @RequestMapping(path = "/request-money/{id}", method = RequestMethod.POST)
    public void requestMoney(@PathVariable int id, @RequestBody @Valid Transfers transfer, Principal principal) {
        accountDAO.transfer(transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());
        List<User> allUsers = userDAO.findAll();
        int receiverId = userDAO.findIdByUsername(principal.getName());
        BalanceData requestBalance = accountDAO.getBalanceGivenAnId(id);

        if(requestBalance.getBalance().compareTo(transfer.getAmount()) >= 0) {
            //searching for the userID I want to request money from
            for(User user: allUsers) {
                if(user.getId() == id) {
                    accountDAO.requestMoney(receiverId, id, transfer.getAmount());
                }
            }
        } else {
            System.out.println("Unable to process request.");
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/transfer/", method = RequestMethod.POST)
    public void transferRequest(@RequestBody @Valid Transfers transfer, Principal principal) {
        accountDAO.transfer(transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount());

        List<User> allUsers = userDAO.findAll();
        int fromId = userDAO.findIdByUsername(principal.getName());
        BalanceData fromBalance = accountDAO.getBalanceGivenAnId(fromId);

        //doing a sufficient funds check
        if(fromBalance.getBalance().compareTo(transfer.getAmount()) >= 0) {
            //searching for the user that I want to send money to
            for(User user: allUsers) {
                if(user.getId() == transfer.getAccountTo()) {
                    accountDAO.sendMoney(transfer.getAccountTo(), fromId, transfer.getAmount());
                }
            }
        } else {
            System.out.println("Insufficient funds in your account.");
        }
    }

    @RequestMapping(path = "/see-all-transfers/{currentUserId}", method = RequestMethod.GET)
    public List<Transfers> seeAllTransfers(@PathVariable int currentUserId, Principal principal) {
        return accountDAO.seeAllTransfers(currentUserId);
    }

    @RequestMapping(path = "/see-transfer-details/{transferId}", method = RequestMethod.GET)
    public List<TransferDetails> seeTransferDetails(@PathVariable int transferId) {
        return transfersDAO.seeTransferDetails(transferId);
    }


}
