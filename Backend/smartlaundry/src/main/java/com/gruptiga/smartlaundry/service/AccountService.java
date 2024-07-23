package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount (AccountRequest accountRequest);

    AccountResponse getById (String accountId);

    List<AccountResponse> getAllAccounts (SearchAccountRequest accountRequest);


    long count();
}
