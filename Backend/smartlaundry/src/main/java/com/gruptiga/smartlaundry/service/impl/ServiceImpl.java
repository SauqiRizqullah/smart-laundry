package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.response.ServicesResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.repository.ServiceRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.ServiceServices;
import com.gruptiga.smartlaundry.validation.ServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceImpl  implements ServiceServices {

    private final ServiceRepository serviceRepository;
    private final AccountService accountService;
    @Autowired
    private final ServiceValidator serviceValidator;


    @Override
    public ServicesResponse createService(ServiceRequest request) {
        Account account = accountService.getByEmail(request.getEmailAccount());

//        customerValidator.validateCustomerRequest(request);
//        customerValidator.validateDuplicateCustomer(request, account);

        Service service = Service.builder()
                .name(request.getName())
                .account(account)
                .build();

        serviceValidator.validateCreateServiceRequest(request,service);


        serviceRepository.saveAndFlush(service);

        return parseServiceResponse(service);
    }

    @Override
    public Service getById(String serviceId) {
        return serviceRepository.findById(serviceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Service tidak ditemukan!!!"));
    }

    @Override
    public List<ServicesResponse> getAllServicesByEmail(SearchCustomerRequest request) {
        return List.of();
    }

    @Override
    @Transactional
    public ServicesResponse updateService(Service service, ServiceRequest serviceRequest) {
       Service services = serviceRepository.findById(service.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service ID not found"));



        services.setName(serviceRequest.getName());
//        serviceValidator.validateUpdateServiceRequest(services);
        serviceRepository.saveAndFlush(services);

        return parseServiceResponse(service);
    }

    @Override
    public ServicesResponse deleteById(String serviceId) {
        Service service = serviceRepository.findById(serviceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Customer tidak ditemukan!!!"));
        serviceRepository.delete(service);
        return parseServiceResponse(service);
    }

    @Override
    public long count() {
        return 0;
    }

    private ServicesResponse parseServiceResponse(Service service) {
        String id = service.getServiceId() == null ? null : service.getServiceId();

        return ServicesResponse.builder()
                .serviceId(id)
                .name(service.getName())
                .build();
    }
}
