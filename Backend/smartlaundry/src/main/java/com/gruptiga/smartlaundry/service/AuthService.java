package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.AuthRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.LoginResponse;

public interface AuthService {
    AccountResponse register(AccountRequest request);
    LoginResponse login(AuthRequest request);
}
