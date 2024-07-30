package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.AuthRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.response.AccountResponse;
import com.gruptiga.smartlaundry.dto.response.LoginResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.repository.ServiceRepository;
import com.gruptiga.smartlaundry.service.AuthService;
import com.gruptiga.smartlaundry.service.JwtService;
import com.gruptiga.smartlaundry.service.ServiceServices;
import com.gruptiga.smartlaundry.validation.AccountValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final AccountValidator accountValidator;

    @Autowired
    private final ServiceRepository serviceRepository;




    @PreAuthorize("permitAll()")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccountResponse register(AccountRequest request) throws DataIntegrityViolationException {
        // Validate the account request
        accountValidator.validateAccountRequest(request);

        // Check if the email already exists
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        // Encode the password
        String hashPassword = passwordEncoder.encode(request.getPassword());

        // Create the Account entity
        Account account = Account.builder()
                .name(request.getName())
                .address(request.getAddress())
                .contact(request.getContact())
                .email(request.getEmail())
                .password(hashPassword)
                .build();

        // Save the Account entity
        Account savedAccount = userAccountRepository.saveAndFlush(account);

        // Define the list of predefined services
        List<ServiceRequest> predefinedServices = Arrays.asList(
                new ServiceRequest(null, "Cuci dan Lipat", request.getEmail()),
                new ServiceRequest(null, "Dry Cleaning", request.getEmail()),
                new ServiceRequest(null, "Setrika/Pressing", request.getEmail()),
                new ServiceRequest(null, "Penghapusan Noda", request.getEmail()),
                new ServiceRequest(null, "Penjahitan dan Perbaikan", request.getEmail()),
                new ServiceRequest(null, "Layanan Antar Jemput", request.getEmail()),
                new ServiceRequest(null, "Pembersihan Barang Khusus", request.getEmail()),
                new ServiceRequest(null, "Layanan Laundry Komersial", request.getEmail())
        );

        // Create and save Service entities
        for (ServiceRequest serviceRequest : predefinedServices) {
            com.gruptiga.smartlaundry.entity.Service service = com.gruptiga.smartlaundry.entity.Service.builder()
                    .name(serviceRequest.getName())
                    .account(savedAccount) // Attach the saved Account entity
                    .build();

            serviceRepository.saveAndFlush(service);
        }

        // Return the AccountResponse
        return AccountResponse.builder()
                .accountId(savedAccount.getAccountId())
                .name(savedAccount.getName())
                .address(savedAccount.getAddress())
                .contact(savedAccount.getContact())
                .email(savedAccount.getEmail())
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
                .email(userAccount.getEmail())
                .build();
    }
}
