package com.gruptiga.smartlaundry.validation;

import com.gruptiga.smartlaundry.constant.EmailPattern;
import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

@Component
public class CustomerValidator {

    @Autowired
    private CustomerRepository customerRepository;

    public void validateDuplicateCustomer(CustomerRequest request) {
        if (customerRepository.existsByNameAndAddressAndPhoneNumber(
                request.getName(),
                request.getAddress(),
                request.getPhoneNumber()
        )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer already exists");
        }
    }

    public void validateCustomerRequest(CustomerRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must not be empty");
        }

        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address must not be empty");
        }

        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must not be empty");
        } else if (!Pattern.matches("^08\\d{9,11}$", request.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must start with 08 and have 9 to 12 digits");
        }

        if (request.getEmailAccount() == null || request.getEmailAccount().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email account must not be empty");
        } else if (!EmailPattern.EMAIL_PATTERN.matcher(request.getEmailAccount()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email format is invalid");
        }
    }

    public void validateUpdateCustomerRequest(Customer request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        // Validate customerId
        if (request.getCustomerId() == null || request.getCustomerId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID must not be empty");
        }

        // Validate name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name must not be empty");
        }

        // Validate address
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address must not be empty");
        }

        // Validate phone number
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must not be empty");
        } else if (!EmailPattern.PHONE_PATTERN.matcher(request.getPhoneNumber()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone number must start with 08 and have 9 to 12 digits");
        }
    }
}
