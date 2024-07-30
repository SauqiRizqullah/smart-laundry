package com.gruptiga.smartlaundry.validation;


import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.repository.CustomerRepository;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import com.gruptiga.smartlaundry.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionValidator {

    @Autowired
    private CustomerRepository customerRepository;
    private final ServiceTypeService serviceType;


    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    public void validateCreateTransactionRequest(TransactionRequest request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction request cannot be null");
        }

        if (request.getCustomersId() == null || request.getCustomersId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer ID must not be empty");
        }

        if (request.getServiceId() == null || request.getServiceId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service ID must not be empty");
        }

        if (request.getQty() == null || request.getQty() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be greater than zero");
        }


        if (request.getTypeId() == null || request.getTypeId().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type ID must not be empty");        }

        if (request.getPayment() == null || request.getPayment().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment method must not be empty");
        }

//        ServiceType serviceType1 = serviceType.getServiceTypeByAccountServiceAndType(accountss, service, type);



        Optional<Customer> customerOptional = customerRepository.findById(request.getCustomersId());
        if (!customerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer with the given ID not found");
        }

//        Optional<ServiceType> serviceTypeOptional = serviceTypeRepository.findById(request.getServiceTypeId());
//        if (!serviceTypeOptional.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service Type with the given ID not found");
//        }
    }
}