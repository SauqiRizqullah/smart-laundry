package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.*;
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
    public ResponseEntity<Page<CustomerResponse>> getCustomersByEmail(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<CustomerResponse> customers = accountService.getCustomersByEmailAndKeyword(email, keyword, page, size);
        return ResponseEntity.ok(customers);
    }

    @GetMapping(APIUrl.SERVICETYPE_ACCOUNT)
    public ResponseEntity<Page<ServiceTypeResponse>> getServiceTypesByEmail(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<ServiceTypeResponse> serviceTypes = accountService.getServiceTypesByEmailAndKeyword(email, keyword, page, size);
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping(APIUrl.SERVICE_ACCOUNT)
    public ResponseEntity<Page<ServicesResponse>> getServiceByEmail(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<ServicesResponse> serviceTypes = accountService.findServicesByAccountEmailAndKeyword(email, keyword, page, size);
        return ResponseEntity.ok(serviceTypes);
    }

    @GetMapping(APIUrl.TYPE_ACCOUNT)
    public ResponseEntity<Page<TypeResponse>> getTypeByEmail(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TypeResponse> typeResponses = accountService.findTypeByAccountEmailAndKeyword(email, keyword, page, size);
        return ResponseEntity.ok(typeResponses);
    }


}
