package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.SearchAccountRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = APIUrl.ACCOUNT)
public class AccountController {
    private final AccountService accountService;

    @GetMapping(path = APIUrl.PATH_VAR_ACCOUNT_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<AccountResponse>> getById (
            @PathVariable String accountId
    ) {
        AccountResponse accountResponse = accountService.getById(accountId);
        CommonResponse<AccountResponse> response = CommonResponse.<AccountResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data akun laundry berhasil didapatkan!!!")
                .data(accountResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<List<AccountResponse>>> getAllAccounts (
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchAccountRequest accountRequest = SearchAccountRequest.builder()
                .name(name)
                .build();

        List<AccountResponse> allCustomers = accountService.getAllAccounts(accountRequest);

        CommonResponse<List<AccountResponse>> response = CommonResponse.<List<AccountResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Semua data akun laundry berhasil didapatkan!!!")
                .data(allCustomers)
                .build();

        return ResponseEntity.ok(response);
    }
}
