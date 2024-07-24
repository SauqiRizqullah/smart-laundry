package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.entity.Account;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount (AccountRequest accountRequest);

    Account getById (String accountId);

    List<AccountResponse> getAllAccounts (SearchAccountRequest accountRequest);


    long count();
}
