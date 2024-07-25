package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.ServiceTypeService;
import com.gruptiga.smartlaundry.service.UserService;
import com.gruptiga.smartlaundry.specification.ServiceTypeSpecification;
import lombok.RequiredArgsConstructor;
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

    private final UserService userService;


    @Override
    public ServiceTypeResponse createServiceType(ServiceTypeRequest serviceTypeRequest) {



        Account currenAccount = userService.getByContext();

        if (!currenAccount.getAccountId().equals(serviceTypeRequest.getAccountId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tidak diizinkan membuat data pelayanan laundry untuk akun laundry yang lain.");
        }

        Account account = accountService.getById(serviceTypeRequest.getAccountId());

        ServiceType serviceType = ServiceType.builder()
                .type(Type.valueOf(serviceTypeRequest.getType()))
                .service(serviceTypeRequest.getService())
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
                .accountId(serviceType.getAccount().getAccountId())
                .type(serviceType.getType().toString())
                .service(serviceType.getService())
                .price(serviceType.getPrice())
                .detail(serviceType.getDetail().toString().toLowerCase())
                .build();
    }

    @Override
    public ServiceType getById(String serviceTypeId) {
        ServiceType serviceType = serviceTypeRepository.findById(serviceTypeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id pelayanan laundry tidak ditemukan!!!"));

        Account currenAccount = userService.getByContext();

        if (!currenAccount.getAccountId().equals(serviceType.getAccount().getAccountId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tidak diizinkan mengambil data pelayanan laundry untuk akun laundry yang lain.");
        }

        return serviceType;
    }

    @Override
    public List<ServiceTypeResponse> getAllServicesType(SearchServiceTypeRequest serviceTypeRequest) {
        Account currentAccount = userService.getByContext();

        // Menambahkan filter berdasarkan accountId dari context
        Specification<ServiceType> serviceTypeSpecification = ServiceTypeSpecification.getSpecification(serviceTypeRequest)
                .and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("account").get("accountId"), currentAccount.getAccountId()));

        // Mencari service type berdasarkan filter yang telah ditentukan
        List<ServiceType> serviceTypes = serviceTypeRepository.findAll(serviceTypeSpecification);

        // Mengonversi hasil menjadi ServiceTypeResponse
        return serviceTypes.stream().map(this::parseServiceTypeToServiceTypeResponse).toList();
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
}
