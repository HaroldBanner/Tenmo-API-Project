package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;
    private JdbcUserDao jdbcUserDao;
    private List<Transfer> transferList = new ArrayList<>();

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transfer> listTransfers() {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM public.transfer;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Transfer transfer = mapRowToTransfer(sqlRowSet);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public List<TransferDetails> listTransfersWithAllDetails() {
        List<TransferDetails> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                "JOIN account " +
                "ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user " +
                "ON account.user_id = tenmo_user.user_id;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            TransferDetails transferDetails = mapRowToTransferDetails(sqlRowSet);
            transferList.add(transferDetails);
        }
        return transferList;
    }

    @Override
    public List<Transfer> listTransfersByUserId(int userId) {
        List<Transfer> transfersById = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account " +
                "ON transfer.account_from = account.account_id " +
                "WHERE user_id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (sqlRowSet.next()) {
            Transfer transfer = mapRowToTransfer(sqlRowSet);
            transfersById.add(transfer);
        }
        return transfersById;
    }

    @Override
    public List<Transfer> transferDetailsByTransferId() {
        return null;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) {

        Account accountFrom = new Account();
        String sqlAcc = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE account_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlAcc, transfer.getAccountFrom());
        while (result.next()) {
            accountFrom = mapRowSetToAccount(result);
        }
        Double accountFromBalance = accountFrom.getBalance().doubleValue();
        Double transferAmountD = transfer.getAmount().doubleValue();
        if (accountFromBalance < transferAmountD) {
            System.out.println("Insufficient Balance");
            return null;
        }


        else if (accountFrom.getAccountId() == transfer.getAccountTo()) {
            System.out.println("Cannot pay yourself, chief :(");
            return null;
        }

        // Create a new transfer row in the transfer table
        String sql = "INSERT INTO transfer ( transfer_type_id, " +
                "transfer_status_id, account_from, account_to, amount) " +
                "VALUES ( ?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        return getTransferByTransferId(newId);
    }

    private Transfer getTransferByTransferId(Integer newId) {
        Transfer transfer = new Transfer();
        return transfer;
    }

    private Account mapRowSetToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }


    @Override
    public boolean requestTransfer() {
        return false;
    }

    @Override
    public boolean approveTransfer() {
        return false;
    }

    @Override
    public boolean rejectTransfer() {
        return false;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

    private TransferDetails mapRowToTransferDetails(SqlRowSet sqlRowSet) {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setTransferId(sqlRowSet.getInt("transfer_id"));
        transferDetails.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
        transferDetails.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transferDetails.setAccountFrom(sqlRowSet.getInt("account_from"));
        transferDetails.setAccountTo(sqlRowSet.getInt("account_to"));
        transferDetails.setAmount(sqlRowSet.getBigDecimal("amount"));
        transferDetails.setAccountId(sqlRowSet.getInt("account_id"));
        transferDetails.setUserId(sqlRowSet.getInt("user_id"));
        transferDetails.setBalance(sqlRowSet.getBigDecimal("balance"));
        transferDetails.setUserIdAccountFrom(sqlRowSet.getInt("user_id"));
        transferDetails.setUsername(sqlRowSet.getString("username"));
        transferDetails.setPassword(sqlRowSet.getString("password_hash"));
        return transferDetails;
    }

}
