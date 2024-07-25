package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.AuthRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.LoginResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.service.AuthService;
import com.gruptiga.smartlaundry.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    @PreAuthorize("permitAll()")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccountResponse register(AccountRequest request) throws DataIntegrityViolationException {

        String hashPassword = passwordEncoder.encode(request.getPassword());

        Account account = Account.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .email(request.getEmail())
                .password(hashPassword)
                .build();

        userAccountRepository.saveAndFlush(account);

        return AccountResponse.builder()
                .accountId(account.getAccountId())
                .name(account.getName())
                .address(account.getAddress())
                .contact(account.getContact())
                .email(account.getEmail())
                .build();

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


    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {

        // baru daftar aja
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        // nah ini pengecheck'an, berarti hasilnya data yg sudah valid
        Authentication authenticated = authenticationManager.authenticate(authentication);

        Account userAccount = (Account) authenticated.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .token(token)
                .name(userAccount.getName())
                .build();
    }
}
