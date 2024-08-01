package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {

    @Modifying
    @Query(value = "UPDATE Transaction t SET t.status = :newStatus WHERE t.trxId = :id")
    void updateStatusById(@Param("id")String id, @Param("newStatus") String newStatus);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.orderDate = :orderDate AND " +
            "(t.customerId LIKE %:keyword% OR t.serviceTypeId LIKE %:keyword%)")
    Page<Transaction> findTransactionsByAccountIdAndOrderDateAndKeyword(
            @Param("accountId") String accountId,
            @Param("orderDate") LocalDate orderDate,
            @Param("keyword") String keyword,
            Pageable pageable
    );


    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND (t.trxId LIKE %:keyword% OR t.customerId LIKE %:keyword% OR t.serviceTypeId LIKE %:keyword%)")
    Page<Transaction> findTransactionsByAccountEmailAndKeyword(@Param("email") String email, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND t.statusPembayaran = :statusPembayaran AND (t.trxId LIKE %:keyword% OR t.customerId LIKE %:keyword% OR t.serviceTypeId LIKE %:keyword%)")
    Page<Transaction> findTransactionsByAccountEmailAndStatusPembayaranAndKeyword(@Param("email") String email, @Param("statusPembayaran") STATUS_PEMBAYARAN statusPembayaran, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND t.status = :status AND (t.trxId LIKE %:keyword% OR t.customerId LIKE %:keyword% OR t.serviceTypeId LIKE %:keyword%)")
    Page<Transaction> findTransactionsByAccountEmailAndStatusAndKeyword(@Param("email") String email, @Param("status") Status status, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND t.status = :status AND t.statusPembayaran = :statusPembayaran AND (t.trxId LIKE %:keyword% OR t.customerId LIKE %:keyword% OR t.serviceTypeId LIKE %:keyword%)")
    Page<Transaction> findTransactionsByAccountEmailAndStatusAndStatusPembayaranAndKeyword(@Param("email") String email, @Param("status") Status status, @Param("statusPembayaran") STATUS_PEMBAYARAN statusPembayaran, @Param("keyword") String keyword, Pageable pageable);


}
