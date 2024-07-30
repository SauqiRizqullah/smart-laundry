package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
//import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Type;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.ServiceServices;
import com.gruptiga.smartlaundry.service.ServiceTypeService;
import com.gruptiga.smartlaundry.service.TypeService;
import com.gruptiga.smartlaundry.specification.ServiceTypeSpecification;
import com.gruptiga.smartlaundry.validation.ServiceTypeValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;

    private final AccountService accountService;
    private final ServiceServices serviceServices;
    private final TypeService typeService;

    @Autowired
    ServiceTypeValidator serviceTypeValidator;


    @Override
    public ServiceTypeResponse createServiceType(ServiceTypeRequest serviceTypeRequest) {
        Account account = accountService.getByEmail(serviceTypeRequest.getEmail());
        Type type = typeService.getById(serviceTypeRequest.getTypeId());
        com.gruptiga.smartlaundry.entity.Service service = serviceServices.getById(serviceTypeRequest.getServiceId());
        serviceTypeValidator.validateCreateServiceTypeRequest(serviceTypeRequest, account, service, type);

        ServiceType serviceType = ServiceType.builder()
                .type(type)
                .service(service)
                .price(serviceTypeRequest.getPrice())
                .detail(Detail.valueOf(serviceTypeRequest.getDetail().toUpperCase()))
                .account(account)
                .build();

        serviceTypeRepository.saveAndFlush(serviceType);

        return parseServiceTypeToServiceTypeResponse(serviceType);
    }

    private ServiceTypeResponse parseServiceTypeToServiceTypeResponse(ServiceType serviceType) {
        String id;
        if (serviceType.getServiceTypeId() == null){
            id = null;
        } else {
            id = serviceType.getServiceTypeId();
        }

        return ServiceTypeResponse.builder()
                .serviceTypeId(id)
                .type(serviceType.getType())
                .service(serviceType.getService())
                .price(serviceType.getPrice())
                .detail(serviceType.getDetail().toString().toLowerCase())
                .build();
    }

    @Override
    public ServiceType getById(String serviceTypeId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id pelayanan laundry tidak ditemukan!!!"));

        return serviceType;
    }

    @Override
    public List<ServiceTypeResponse> getAllServicesType(SearchServiceTypeRequest serviceTypeRequest) {
        Specification<ServiceType> serviceTypeSpecification = ServiceTypeSpecification.getSpecification(serviceTypeRequest);
        if(serviceTypeRequest.getType() == null && serviceTypeRequest.getService() == null){
            return serviceTypeRepository.findAll().stream().map(this::parseServiceTypeToServiceTypeResponse).toList();
        } else {
            return serviceTypeRepository.findAll(serviceTypeSpecification).stream().map(this::parseServiceTypeToServiceTypeResponse).toList();
        }
    }

    @Override
    @Transactional
    public ServiceTypeResponse updateServiceType(ServiceType serviceType) {
        serviceTypeValidator.validateUpdateServiceTypeRequest(serviceType);

        serviceTypeRepository.findById(serviceType.getServiceTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer ID not found"));

        serviceTypeRepository.saveAndFlush(serviceType);

        return parseServiceTypeToServiceTypeResponse(serviceType);
    }

    @Override
    public long count() {
        return serviceTypeRepository.count();
    }

    @Override
    public ServiceTypeResponse deleteById(String serviceTypeId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id pelayanan laundry tidak ditemukan!!!"));
        serviceTypeRepository.delete(serviceType);
        return parseServiceTypeToServiceTypeResponse(serviceType);
    }

    @Override
    public ServiceType getServiceTypeByAccountServiceAndType(Account account, com.gruptiga.smartlaundry.entity.Service service, Type type) {
        return serviceTypeRepository.findByAccountAndServiceAndType(account, service, type)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service Type ID not found"));
    }


}
