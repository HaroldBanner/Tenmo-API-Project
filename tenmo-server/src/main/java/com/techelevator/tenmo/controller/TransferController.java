package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransferController {

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    // Alex changed return type to void, can get HttpStatus message back when we add the tag
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void createTransfer(@RequestBody Transfer transfer) {
        transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public List<Transfer> transfersById(@PathVariable int id) {
        System.out.println(id);
        return transferDao.listTransfersByUserId(id);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> listTransfers() {
        return transferDao.listTransfers();
    }

    @RequestMapping(path = "/transfer/all", method = RequestMethod.GET)
    public List<TransferDetails> listTransfersWithDetails() {
        return transferDao.listTransfersWithAllDetails();
    }
}
