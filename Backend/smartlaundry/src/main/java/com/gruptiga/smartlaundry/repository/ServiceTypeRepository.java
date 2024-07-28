package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.constant.Type;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, String>, JpaSpecificationExecutor<ServiceType> {
    boolean existsByTypeAndServiceAndDetailAndAccount(Type type, String service, Detail detail, Account account);

}
