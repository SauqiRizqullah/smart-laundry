package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {

    @Modifying
    @Query(value = "UPDATE Transaction t SET t.status = :newStatus WHERE t.trxId = :id")
    void updateStatusById(@Param("id")String id, @Param("newStatus") String newStatus);

}
