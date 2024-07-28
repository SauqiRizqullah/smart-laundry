package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Transaction;
import com.gruptiga.smartlaundry.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(APIUrl.ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping(path = APIUrl.PATH_VAR_ACCOUNT_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<Account>> getById(@PathVariable String accountId) {
        Account accountResponse = accountService.getById(accountId);
        CommonResponse<Account> response = CommonResponse.<Account>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data akun berhasil didapatkan!!!")
                .data(accountResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<List<AccountResponse>>> getAllAccounts(
            @RequestParam(name = "name", required = false) String name) {
        SearchAccountRequest accountRequest = SearchAccountRequest.builder()
                .name(name)
                .build();

        List<AccountResponse> allAccounts = accountService.getAllAccounts(accountRequest);

        CommonResponse<List<AccountResponse>> response = CommonResponse.<List<AccountResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Semua data akun berhasil didapatkan!!!")
                .data(allAccounts)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = APIUrl.ACCOUNT_BY_EMAIL, produces = "application/json")
    public ResponseEntity<CommonResponse<Account>> getByEmail(
            @RequestParam String email
    ) {
        Account account = accountService.getByEmail(email);

        account.setTransactions(null);
        account.setServiceTypes(null);
        account.setCustomers(null);

        CommonResponse<Account> response = CommonResponse.<Account>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data akun laundry berhasil didapatkan!!!")
                .data(account)
                .build();

        return ResponseEntity.ok(response);
    }


    @PutMapping(APIUrl.UPDATE)
    public ResponseEntity<String> updateAccount(@RequestParam String email, @RequestBody AccountRequest request) {
        try {
            accountService.updateAccount(email, request);
            return new ResponseEntity<>("Account updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(APIUrl.CUSTOMER_ACCOUNT)
    public ResponseEntity<Page<Customer>> getCustomersByEmail(@RequestParam String email, Pageable pageable) {
        Page<Customer> customers = accountService.getCustomersByEmail(email, pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(APIUrl.SERVICETYPE_ACCOUNT)
    public ResponseEntity<Page<ServiceType>> getServiceTypesByEmail(@RequestParam String email, Pageable pageable) {
        Page<ServiceType> serviceTypes = accountService.getServiceTypesByEmail(email, pageable);
        return new ResponseEntity<>(serviceTypes, HttpStatus.OK);
    }

    @GetMapping(APIUrl.TRANSACTION_ACCOUNT)
    public ResponseEntity<Page<Transaction>> getTransactions(@RequestParam String email, Pageable pageable) {
        Page<Transaction> transactions = accountService.getTransactionsByAccountEmail(email, pageable);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping(APIUrl.BYSTATUSPembayaran_ACCOUNT)
    public ResponseEntity<Page<Transaction>> getTransactionsByAccountEmailAndStatusPembayaran(
            @RequestParam String email,
            @RequestParam STATUS_PEMBAYARAN statusPembayaran,
            Pageable pageable) {
        Page<Transaction> transactions = accountService.getTransactionsByAccountEmailAndStatusPembayaran(email, statusPembayaran, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(APIUrl.BYSTATUS_ACCOUNT)
    public ResponseEntity<Page<Transaction>> getTransactionsByAccountEmailAndStatus(
            @RequestParam String email,
            @RequestParam Status status,
            Pageable pageable) {
        Page<Transaction> transactions = accountService.getTransactionsByAccountEmailAndStatus(email, status, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(APIUrl.BYSTATUS_Pembayaran_ACCOUNT)
    public ResponseEntity<Page<Transaction>> getTransactionsByAccountEmailAndStatusAndPembayaran(
            @RequestParam String email,
            @RequestParam Status status,
            @RequestParam STATUS_PEMBAYARAN statusPembayaran,
            Pageable pageable) {
        Page<Transaction> transactions = accountService.getTransactionsByAccountEmailAndStatusAndStatusPembayaran(email, status, statusPembayaran, pageable);
        return ResponseEntity.ok(transactions);
    }
}
