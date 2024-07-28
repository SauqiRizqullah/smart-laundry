package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
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
    Page<CustomerResponse> getCustomersByEmailAndKeyword(String email, String keyword, int page, int size);

    Page<ServiceTypeResponse> getServiceTypesByEmailAndKeyword(String email, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailAndKeyword(String email, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailStatusPembayaranAndKeyword(String email, STATUS_PEMBAYARAN statusPembayaran, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailStatusAndKeyword(String email, Status status, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailStatusStatusPembayaranAndKeyword(String email, Status status, STATUS_PEMBAYARAN statusPembayaran, String keyword, int page, int size);

}
