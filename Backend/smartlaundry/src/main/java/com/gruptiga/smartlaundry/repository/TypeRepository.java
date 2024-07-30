package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TypeRepository   extends JpaRepository<Type, String>, JpaSpecificationExecutor<Type> {
//    boolean existsByTypeAndAccount(Type type, Account account);
//    Optional<Type> findByNameAndAccount(String name, Account account);
}
