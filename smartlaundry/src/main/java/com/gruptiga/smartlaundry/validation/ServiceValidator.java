package com.gruptiga.smartlaundry.validation;

import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.request.TypeRequest;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Type;
import com.gruptiga.smartlaundry.repository.ServiceRepository;
import com.gruptiga.smartlaundry.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ServiceValidator {

    @Autowired
    private ServiceRepository serviceRepository;


    public void validateCreateServiceRequest(ServiceRequest serviceRequest, Service service) {
        if (serviceRequest.getName() == null || serviceRequest.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
        }

        boolean exists = serviceRepository.existsByNameAndAccount(service.getName(), service.getAccount());
        if (exists) {
            throw new IllegalArgumentException("Type with the same name and account already exists");
        }
    }

    public void validateUpdateServiceRequest(Service service) {
        boolean exists = serviceRepository.existsByNameAndAccount(service.getName(), service.getAccount());
        if (exists) {
            throw new IllegalArgumentException("Type with the same name and account already exists");
        }
    }

}
