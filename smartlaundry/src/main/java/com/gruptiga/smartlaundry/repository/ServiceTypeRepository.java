package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.constant.Detail;
//import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, String>, JpaSpecificationExecutor<ServiceType> {
    boolean existsByTypeAndServiceAndDetailAndAccount(Type type, Service service, Detail detail, Account account);
    Optional<ServiceType> findByAccountAndServiceAndType(Account account, Service service, Type type);

}
