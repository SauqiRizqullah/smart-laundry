package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceRepository  extends JpaRepository<Service, String>, JpaSpecificationExecutor<Service> {
    boolean existsByNameAndAccount(String name, Account account);

    boolean existsByNameAndAccountAndServiceIdNot(String name, Account account, String serviceId);

}
