package com.gruptiga.smartlaundry.validation;

import com.gruptiga.smartlaundry.constant.EmailPattern;
import com.gruptiga.smartlaundry.dto.request.AccountRequest;
import com.gruptiga.smartlaundry.dto.request.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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

        // Validate phone number
        if (request.getContact() == null || request.getContact().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must not be empty");
        } else if (!EmailPattern.PHONE_PATTERN.matcher(request.getContact()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must start with 08 and have 9 to 12 digits");
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
