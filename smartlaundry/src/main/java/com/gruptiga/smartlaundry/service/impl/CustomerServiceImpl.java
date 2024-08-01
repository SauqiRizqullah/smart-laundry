package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.repository.CustomerRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.CustomerService;
import com.gruptiga.smartlaundry.specification.CustomerSpecification;
import com.gruptiga.smartlaundry.validation.CustomerValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    @Autowired
    private CustomerValidator customerValidator;

    @Override
    @Transactional
    public CustomerResponse createCustomer(@Valid CustomerRequest request) {

        Account account = accountService.getByEmail(request.getEmailAccount());

        customerValidator.validateCustomerRequest(request);
        customerValidator.validateDuplicateCustomer(request, account);


        Customer customer = Customer.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .account(account)
                .build();

        customerRepository.saveAndFlush(customer);

        return parseCustomerToCustomerResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(@Valid Customer customer, CustomerRequest customerRequest) {

        Customer customer1 = customerRepository.findById(customer.getCustomerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer ID not found"));

        customer1.setAddress(customerRequest.getAddress());
        customer1.setName(customerRequest.getName());
        customer1.setPhoneNumber(customerRequest.getPhoneNumber());

        customerValidator.validateUpdateCustomerRequest(customer1);

        customerRepository.saveAndFlush(customer);

        return parseCustomerToCustomerResponse(customer);
    }

    private CustomerResponse parseCustomerToCustomerResponse(Customer customer) {
        String id = customer.getCustomerId() == null ? null : customer.getCustomerId();

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
