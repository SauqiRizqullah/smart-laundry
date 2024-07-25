package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.repository.AccountRepository;
import com.gruptiga.smartlaundry.repository.CustomerRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.CustomerService;
import com.gruptiga.smartlaundry.service.UserService;
import com.gruptiga.smartlaundry.specification.CustomerSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    private final UserService userService;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        Account account = userService.getByContext();

        if (!request.getAccountId().equals(account.getAccountId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tidak diizinkan membuat data customer untuk akun laundry yang lain.");
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .account(account)
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
                .accountId(customer.getAccount().getAccountId())
                .build();
    }

    @Override
    public Customer getById(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Customer tidak ditemukan!!!"));

        Account account = userService.getByContext();

        if(!account.getAccountId().equals(customer.getAccount().getAccountId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tidak diizinkan mengambil data customer untuk akun laundry yang lain.");
        }

        return customer;
    }

    @Override
    public List<CustomerResponse> getAllCustomers(SearchCustomerRequest request) {
        // Mendapatkan account dari context
        Account currentAccount = userService.getByContext();

        // Menambahkan filter berdasarkan accountId dari context
        Specification<Customer> customerSpecification = CustomerSpecification.getSpecification(request)
                .and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("account").get("accountId"), currentAccount.getAccountId()));

        // Mencari customer berdasarkan filter yang telah ditentukan
        List<Customer> customers = customerRepository.findAll(customerSpecification);

        // Mengonversi hasil menjadi CustomerResponse
        return customers.stream().map(this::parseCustomerToCustomerResponse).toList();
    }

    @Override
    public CustomerResponse updateCustomer(Customer customer) {
        Account currentAccount = userService.getByContext();

        // Menemukan customer berdasarkan ID dan memastikan accountId sesuai dengan context
        Customer existingCustomer = customerRepository.findById(customer.getCustomerId())
                .filter(cust -> cust.getAccount().getAccountId().equals(currentAccount.getAccountId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Customer tidak ditemukan atau tidak memiliki akses!!!"));

        // Update data customer
        existingCustomer.setName(customer.getName());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());

        customerRepository.saveAndFlush(existingCustomer);

        return parseCustomerToCustomerResponse(existingCustomer);
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
