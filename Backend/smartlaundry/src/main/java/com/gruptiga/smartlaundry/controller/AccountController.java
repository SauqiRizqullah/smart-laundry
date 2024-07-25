package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
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

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getCustomersByEmail(@RequestParam String email) {
        List<Customer> customers = accountService.getCustomersByEmail(email);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/serviceTypes")
    public ResponseEntity<List<ServiceType>> getServiceTypesByEmail(@RequestParam String email) {
        List<ServiceType> serviceTypes = accountService.getServiceTypesByEmail(email);
        return new ResponseEntity<>(serviceTypes, HttpStatus.OK);
    }

//    @GetMapping(path = "/transactions", produces = "application/json")
//    public ResponseEntity<CommonResponse<List<Transaction>>> getTransactionsByAccountEmail(
//            @RequestParam(name = "email") String email) {
//        List<Transaction> transactions = accountService.getTransactionsByAccountEmail(email);
//        CommonResponse<List<Transaction>> response = CommonResponse.<List<Transaction>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("Data transaksi berdasarkan email akun berhasil didapatkan!!!")
//                .data(transactions)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
}
