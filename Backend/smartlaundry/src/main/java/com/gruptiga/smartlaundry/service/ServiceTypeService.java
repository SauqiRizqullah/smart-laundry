package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;
import com.gruptiga.smartlaundry.entity.*;

import java.util.List;

public interface ServiceTypeService {
    ServiceTypeResponse createServiceType(ServiceTypeRequest serviceTypeRequest);
    ServiceType getById(String serviceTypeId);

    List<ServiceTypeResponse> getAllServicesType(SearchServiceTypeRequest serviceTypeRequest);

    ServiceTypeResponse updateServiceType (ServiceType serviceType);


    long count();

    ServiceTypeResponse deleteById(String serviceTypeId);

    ServiceType getServiceTypeByAccountServiceAndType(Account account, Service service, Type type);
}
