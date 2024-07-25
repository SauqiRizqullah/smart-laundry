package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.repository.CustomerRepository;
import com.gruptiga.smartlaundry.service.CustomerService;
import com.gruptiga.smartlaundry.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        customerRepository.saveAndFlush(customer);

        return parseCustomerToCustomerResponse(customer);
    }

    private CustomerResponse parseCustomerToCustomerResponse(Customer customer) {
        String id;
        if (customer.getCustomerId() == null) {
            id = null;
        } else {
            id = customer.getCustomerId();
        }

        return CustomerResponse.builder()
                .customerId(id)
                .name(customer.getName())
                .address(customer.getAddress())
                .phoneNumber(customer.getPhoneNumber())
                .build();
    }

    @Override
    public Customer getById(String customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Customer tidak ditemukan!!!"));
    }

    @Override
    public List<CustomerResponse> getAllCustomers(SearchCustomerRequest request) {
        Specification<Customer> customerSpecification = CustomerSpecification.getSpecification(request);
        if (request.getCustomerName() == null){
            return customerRepository.findAll().stream().map(this::parseCustomerToCustomerResponse).toList();
        } else
            return customerRepository.findAll(customerSpecification).stream().map(this::parseCustomerToCustomerResponse).toList();
    }

    @Override
    public CustomerResponse updateCustomer(Customer customer) {
        customerRepository.findById(customer.getCustomerId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Customer tidak ditemukan!!!"));

        customerRepository.saveAndFlush(customer);

        return parseCustomerToCustomerResponse(customer);
    }

    @Override
    public CustomerResponse deleteById(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Customer tidak ditemukan!!!"));
        customerRepository.delete(customer);
        return parseCustomerToCustomerResponse(customer);
    }

    @Override
    public long count() {
        return customerRepository.count();
    }
}
