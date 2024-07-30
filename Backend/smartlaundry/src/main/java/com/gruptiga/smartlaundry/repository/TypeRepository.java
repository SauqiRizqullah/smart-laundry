package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeRepository   extends JpaRepository<Type, String>, JpaSpecificationExecutor<Type> {
}
