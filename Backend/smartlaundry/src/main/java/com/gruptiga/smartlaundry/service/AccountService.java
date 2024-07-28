package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Transaction;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    Account getById(String accountId);
    Account getByEmail(String email);
    List<AccountResponse> getAllAccounts(SearchAccountRequest accountRequest);
    long count();
    List<Customer> getCustomersByEmail(String email);
    List<ServiceType> getServiceTypesByEmail(String email);
    void updateAccount(String email, AccountRequest request);
    List<Transaction> findTransactionsByAccountEmail(String email);
    List<Transaction> getTransactionsByAccountEmailAndStatusPembayaran(String email, STATUS_PEMBAYARAN statusPembayaran);
    List<Transaction> getTransactionsByAccountEmailAndStatus(String email, Status status);
    List<Transaction> getTransactionsByAccountEmailAndStatusAndStatusPembayaran(String email, Status status, STATUS_PEMBAYARAN statusPembayaran);

}
