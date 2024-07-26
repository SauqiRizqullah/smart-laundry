package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
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

}
