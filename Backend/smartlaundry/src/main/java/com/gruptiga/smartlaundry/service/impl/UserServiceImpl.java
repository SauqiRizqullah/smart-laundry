package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.BlacklistedToken;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.repository.TokenRepository;
import com.gruptiga.smartlaundry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AccountRepository userAccountRepository;

    private final TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    @Override
    // bukan kita yg manggil
    // yang menangani proses login yg dikelola oleh AuthenticationManager
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public Account getByUserId(String id) {
        return userAccountRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );
    }

    @Override
    public Account getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userAccountRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found from context")
        );
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() != null) {
            String token = authentication.getCredentials().toString();
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            blacklistedToken.setExpiryDate(Instant.now().plus(1, ChronoUnit.HOURS)); // Sesuaikan waktu kedaluwarsa sesuai kebutuhan Anda
            tokenRepository.save(blacklistedToken);
        }
        SecurityContextHolder.clearContext();
    }
}

