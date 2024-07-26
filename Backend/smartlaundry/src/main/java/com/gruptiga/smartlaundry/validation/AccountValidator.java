package com.gruptiga.smartlaundry.validation;

import com.gruptiga.smartlaundry.constant.EmailPattern;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.AuthRequest;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class AccountValidator {


    public void validateAccountRequest(AccountRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (request.getAddress() == null || request.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address must not be empty");
        }
        if (request.getContact() == null || request.getContact().isEmpty()) {
            throw new IllegalArgumentException("Contact must not be empty");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (!EmailPattern.EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new IllegalArgumentException("Email format is invalid");
        }
    }

    public void validateAuthRequest(AuthRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        if (!EmailPattern.EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new IllegalArgumentException("Email format is invalid");
        }
    }
}
