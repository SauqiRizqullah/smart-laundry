package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.specification.AccountSpecification;
import com.gruptiga.smartlaundry.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = Account
                .builder()
                .name(accountRequest.getName())
                .address(accountRequest.getAddress())
                .contact(accountRequest.getContact())
                .email(accountRequest.getEmail())
                .password(accountRequest.getPassword())
                .build();

        accountRepository.saveAndFlush(account);

        return parseAccountToAccountResponse(account);
    }

    private AccountResponse parseAccountToAccountResponse(Account account) {
        String id;
        if (account.getAccountId() == null) {
            id = null;
        } else {
            id = account.getAccountId();
        }

        return AccountResponse.builder()
                .accountId(id)
                .name(account.getName())
                .address(account.getAddress())
                .contact(account.getContact())
                .email(account.getEmail())
                .build();
    }


    @Override
    public Account getById(String accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Customer tidak ditemukan!!!"));

        return account;
    }

    @Override
    public List<AccountResponse> getAllAccounts(SearchAccountRequest accountRequest) {
        Specification<Account> accountSpecification = AccountSpecification.getSpecification(accountRequest);
        if (accountRequest.getName() == null){
            return accountRepository.findAll().stream().map(this::parseAccountToAccountResponse).toList();
        } else
            return accountRepository.findAll(accountSpecification).stream().map(this::parseAccountToAccountResponse).toList();
    }

    @Override
    public long count() {
        return accountRepository.count();
    }
}
