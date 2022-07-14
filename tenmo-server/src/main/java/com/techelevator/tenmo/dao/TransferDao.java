package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listTransfers();

    List<TransferDetails> listTransfersWithAllDetails();

    List<Transfer> listTransfersByUserId(int userId);

    List<Transfer> transferDetailsByTransferId();

    Transfer createTransfer(Transfer transfer);

    boolean requestTransfer();

    boolean approveTransfer();

    boolean rejectTransfer();

}
