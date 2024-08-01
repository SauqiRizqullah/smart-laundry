package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
//import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;
import com.gruptiga.smartlaundry.dto.response.ServicesResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Image;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Type;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import com.gruptiga.smartlaundry.service.*;
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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ServiceTypeServiceImpl implements ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;

    private final AccountService accountService;
    private final ServiceServices serviceServices;
    private final TypeService typeService;
    private final ImageService imageService;

    @Autowired
    ServiceTypeValidator serviceTypeValidator;


    @Override
    public ServiceTypeResponse createServiceType(ServiceTypeRequest serviceTypeRequest) {
        Account account = accountService.getByEmail(serviceTypeRequest.getEmail());
        Type type = typeService.getById(serviceTypeRequest.getTypeId());

        com.gruptiga.smartlaundry.entity.Service service = null;
        ServicesResponse servicesResponse = null;

        if (!serviceServices.existsById(serviceTypeRequest.getServiceRequest().getServiceId())) {
            servicesResponse = serviceServices.createService(serviceTypeRequest.getServiceRequest());
            service = new com.gruptiga.smartlaundry.entity.Service();
            service.setServiceId(servicesResponse.getServiceId());
            service.setName(servicesResponse.getName());
            service.setAccount(servicesResponse.getAccount());
        } else {
            service = new com.gruptiga.smartlaundry.entity.Service();
            service.setServiceId(serviceTypeRequest.getServiceRequest().getServiceId());
            service.setName(serviceTypeRequest.getServiceRequest().getName());
            service.setAccount(account);
        }

        serviceTypeValidator.validateCreateServiceTypeRequest(serviceTypeRequest, account, service, type);

        Image image = null;
        if (Objects.equals(service.getName(), "Cuci Full")){
            image = imageService.searchByName("CuciFull.jpg");
        }

        ServiceType.ServiceTypeBuilder serviceType = ServiceType.builder()
                .type(type)
                .service(service)
                .price(serviceTypeRequest.getPrice())
                .detail(Detail.valueOf(serviceTypeRequest.getDetail().toUpperCase()))
                .account(account);

        if (image != null) {
            serviceType.imagePath(image.getPath());
        }

        ServiceType savedServiceType = serviceTypeRepository.saveAndFlush(serviceType.build());

        if (servicesResponse != null) {
            service.setServiceTypes(servicesResponse.getServiceTypes());
        }

        return parseServiceTypeToServiceTypeResponse(savedServiceType);
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
                .imagePath(serviceType.getImagePath())
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
    public ServiceTypeResponse updateServiceType(ServiceType serviceType, ServiceTypeRequest serviceTypeRequest) {
        serviceTypeValidator.validateUpdateServiceTypeRequest(serviceType);

       ServiceType serviceType1 = serviceTypeRepository.findById(serviceType.getServiceTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer ID not found"));
       Type typeService1 = typeService.getById(serviceTypeRequest.getTypeId());
       com.gruptiga.smartlaundry.entity.Service service = serviceServices.getById(serviceTypeRequest.getServiceRequest().getServiceId());

       serviceType1.setPrice(serviceTypeRequest.getPrice());
       serviceType1.setType(typeService1);
       serviceType1.setService(service);
       serviceType1.setDetail(Detail.valueOf(serviceTypeRequest.getDetail()));
        serviceTypeRepository.saveAndFlush(serviceType1);

        return parseServiceTypeToServiceTypeResponse(serviceType1);
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
