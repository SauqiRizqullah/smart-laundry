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
        CommonResponse<Account> response = CommonResponse.<Account>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data akun laundry berhasil didapatkan!!!")
                .data(account)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(APIUrl.CUSTOMER_ACCOUNT)
    public ResponseEntity<List<Customer>> getCustomersByEmail(@RequestParam String email) {
        List<Customer> customers = accountService.getCustomersByEmail(email);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping(APIUrl.SERVICETYPE_ACCOUNT)
    public ResponseEntity<List<ServiceType>> getServiceTypesByEmail(@RequestParam String email) {
        List<ServiceType> serviceTypes = accountService.getServiceTypesByEmail(email);
        return new ResponseEntity<>(serviceTypes, HttpStatus.OK);
    }

    @GetMapping(APIUrl.TRANSACTION_ACCOUNT)
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam String email) {
        List<Transaction> transactions = accountService.findTransactionsByAccountEmail(email);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
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

    @GetMapping(APIUrl.BYSTATUSPembayaran_ACCOUNT)
    public ResponseEntity<List<Transaction>> getTransactionsByAccountEmailAndStatusPembayaran(
            @RequestParam String email,
            @RequestParam STATUS_PEMBAYARAN statusPembayaran) {
        List<Transaction> transactions = accountService.getTransactionsByAccountEmailAndStatusPembayaran(email, statusPembayaran);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(APIUrl.BYSTATUS_ACCOUNT)
    public ResponseEntity<List<Transaction>> getTransactionsByAccountEmailAndStatus(
            @RequestParam String email,
            @RequestParam Status status) {
        List<Transaction> transactions = accountService.getTransactionsByAccountEmailAndStatus(email, status);
        return ResponseEntity.ok(transactions);
    }
    @GetMapping(APIUrl.BYSTATUS_Pembayaran_ACCOUNT)
    public ResponseEntity<List<Transaction>> getTransactionsByAccountEmailAndStatusAndPembayaran(
            @RequestParam String email,
            @RequestParam Status status,
            @RequestParam STATUS_PEMBAYARAN statusPembayaran
            ) {
        List<Transaction> transactions = accountService.getTransactionsByAccountEmailAndStatusAndStatusPembayaran(email, status, statusPembayaran);
        return ResponseEntity.ok(transactions);
    }
}
