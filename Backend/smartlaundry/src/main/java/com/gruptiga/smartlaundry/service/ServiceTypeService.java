package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;

import java.util.List;

public interface ServiceTypeService {
    ServiceTypeResponse createServiceType(ServiceTypeRequest serviceTypeRequest);
    ServiceTypeResponse getById(String serviceTypeId);

    List<ServiceTypeResponse> getAllServicesType(SearchServiceTypeRequest serviceTypeRequest);



    long count();

    ServiceTypeResponse deleteById(String serviceTypeId);
}
