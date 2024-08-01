package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    boolean existsByNameAndAddressAndPhoneNumberAndAccount(String name, String address, String phoneNumber, Account account);
}
