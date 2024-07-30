package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.ServicesResponse;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.Service;

import java.util.List;

public interface ServiceServices {
    ServicesResponse createService(ServiceRequest request);

    Service getById(String serviceId);

    List<ServicesResponse> getAllServicesByEmail (SearchCustomerRequest request);

    ServicesResponse updateService (Service service, ServiceRequest serviceRequest);

    ServicesResponse deleteById(String serviceId);

    long count();
}
