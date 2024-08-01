package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = APIUrl.USER)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(path = APIUrl.USER_CONTEXT, produces = "application/json")
    public ResponseEntity<CommonResponse<Account>> getUserByContext() {
        Account currentUser = userService.getByContext();
        CommonResponse<Account> response = CommonResponse.<Account>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("successfully save data")
                .data(currentUser)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = APIUrl.LOGOUT, produces = "application/json")
    public ResponseEntity<CommonResponse<String>> logout() {
        userService.logout();
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Berhasil melakukan logout!!!")
                .data("Anda harus login terlebih dahulu untuk mendapatkan akses")
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping(path = APIUrl.USER_CONTEXT, produces = "application/json")
//    public ResponseEntity<CommonResponse<String>> getUserByContextEmail() {
//        Account currentUser = userService.getByContext();
//
//        String email = currentUser.getEmail();
//
//        CommonResponse<String> response = CommonResponse.<String>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message("successfully save data")
//                .data(email)
//                .build();
//        return ResponseEntity.ok(response);
//    }
}
