package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.EmailPattern;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Transaction;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.specification.AccountSpecification;
import com.gruptiga.smartlaundry.validation.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountValidator accountValidator;


    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        Account account = Account.builder()
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
        String id = account.getAccountId() == null ? null : account.getAccountId();

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
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Account tidak ditemukan!!!"));
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email Account tidak ditemukan!!!"));
    }


    @Override
    @Transactional
    public void updateAccount(String email, AccountRequest request) {
        accountValidator.validateAccountRequest(request);

        Optional<Account> existingAccountOptional = accountRepository.findByEmail(email);
        if (!existingAccountOptional.isPresent()) {
            throw new IllegalArgumentException("Account does not exist");
        }

        if (!email.equals(request.getEmail()) && accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("New email is already taken");
        }

        if (!EmailPattern.EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new IllegalArgumentException("New email format is invalid");
        }

        String hashPassword = passwordEncoder.encode(request.getPassword());

        accountRepository.updateAccount(
                email,
                request.getName(),
                request.getAddress(),
                request.getContact(),
                hashPassword
        );
    }

    @Override
    public List<Transaction> findTransactionsByAccountEmail(String email) {
        return accountRepository.findTransactionsByAccountEmail(email);
    }

    @Override
    public List<Transaction> getTransactionsByAccountEmailAndStatusPembayaran(String email, STATUS_PEMBAYARAN statusPembayaran) {
        return accountRepository.findTransactionsByAccountEmailAndStatusPembayaran(email, statusPembayaran);
    }

    @Override
    public List<Transaction> getTransactionsByAccountEmailAndStatus(String email, Status status) {
        return accountRepository.findTransactionsByAccountEmailAndStatus(email, status);
    }

    @Override
    public List<Transaction> getTransactionsByAccountEmailAndStatusAndStatusPembayaran(String email, Status status, STATUS_PEMBAYARAN statusPembayaran) {
        return accountRepository.findTransactionsByAccountEmailAndStatusANDStatusPembayaran(email, status, statusPembayaran);
    }

    @Override
    public List<AccountResponse> getAllAccounts(SearchAccountRequest accountRequest) {
        Specification<Account> accountSpecification = AccountSpecification.getSpecification(accountRequest);
        if (accountRequest.getName() == null) {
            return accountRepository.findAll().stream()
                    .map(this::parseAccountToAccountResponse)
                    .toList();
        } else {
            return accountRepository.findAll(accountSpecification).stream()
                    .map(this::parseAccountToAccountResponse)
                    .toList();
        }
    }

    @Override
    public long count() {
        return accountRepository.count();
    }



    @Override
    public List<Customer> getCustomersByEmail(String email) {
        return accountRepository.findCustomersByAccountEmail(email);
    }

    @Override
    public List<ServiceType> getServiceTypesByEmail(String email) {
        return accountRepository.findServiceTypesByAccountEmail(email);
    }


}
