package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Customer;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest request);

    Customer getById(String customerId);

    List<CustomerResponse> getAllCustomers (SearchCustomerRequest request);

    CustomerResponse updateCustomer (Customer customer);

    CustomerResponse deleteById(String customerId);
}
