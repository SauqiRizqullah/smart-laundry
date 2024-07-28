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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest);
    Account getById(String accountId);
    Account getByEmail(String email);
    List<AccountResponse> getAllAccounts(SearchAccountRequest accountRequest);
    long count();
    void updateAccount(String email, AccountRequest request);
    Page<Customer> getCustomersByEmail(String email, Pageable pageable);

    Page<ServiceType> getServiceTypesByEmail(String email, Pageable pageable);

    Page<Transaction> getTransactionsByAccountEmail(String email, Pageable pageable);

    Page<Transaction> getTransactionsByAccountEmailAndStatusPembayaran(String email, STATUS_PEMBAYARAN statusPembayaran, Pageable pageable);

    Page<Transaction> getTransactionsByAccountEmailAndStatus(String email, Status status, Pageable pageable);

    Page<Transaction> getTransactionsByAccountEmailAndStatusAndStatusPembayaran(String email, Status status, STATUS_PEMBAYARAN statusPembayaran, Pageable pageable);

}
