package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.response.ServicesResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.repository.ServiceRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.ServiceServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceImpl  implements ServiceServices {

    private final ServiceRepository serviceRepository;
    private final AccountService accountService;


    @Override
    public ServicesResponse createService(ServiceRequest request) {
        Account account = accountService.getByEmail(request.getEmailAccount());

//        customerValidator.validateCustomerRequest(request);
//        customerValidator.validateDuplicateCustomer(request, account);

        Service service = Service.builder()
                .name(request.getName())
                .account(account)
                .build();

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
    public ServicesResponse updateCustomer(Service service) {
        serviceRepository.findById(service.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer ID not found"));

        serviceRepository.saveAndFlush(service);

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
