package com.gruptiga.smartlaundry.validation;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@Component
public class ServiceTypeValidator {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    private static final Pattern DETAIL_PATTERN = Pattern.compile("^[A-Z_]+$");

    public void validateCreateServiceTypeRequest(ServiceTypeRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type must not be empty");
        }

        if (request.getService() == null || request.getService().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service must not be empty");
        }

        if (request.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must not be null");
        } else if (request.getPrice() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price must be greater than zero");
        }

        if (request.getDetail() == null || request.getDetail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Detail must not be empty");
        } else if (!DETAIL_PATTERN.matcher(request.getDetail().toUpperCase()).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Detail must be in uppercase and contain only alphabets or underscores");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account email must not be empty");
        }

        boolean exists = serviceTypeRepository.existsByTypeAndServiceAndDetail(
                Type.valueOf(request.getType()),
                request.getService(),
                Detail.valueOf(request.getDetail())
        );
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ServiceType with the same type, service, detail, and account already exists");
        }
    }

}
