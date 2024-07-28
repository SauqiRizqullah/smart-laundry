package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface AccountRepository extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByEmail(String email);


    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Account a SET a.name = :name, a.address = :address, a.contact = :contact, a.password = :password WHERE a.email = :email")
    void updateAccount(
            @Param("email") String email,
            @Param("name") String name,
            @Param("address") String address,
            @Param("contact") String contact,
            @Param("password") String password
    );

    @Query("SELECT c FROM Customer c WHERE c.account.email = :email")
    Page<Customer> findCustomersByAccountEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT s FROM ServiceType s WHERE s.account.email = :email")
    Page<ServiceType> findServiceTypesByAccountEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email")
    Page<Transaction> findTransactionsByAccountEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND t.statusPembayaran = :statusPembayaran")
    Page<Transaction> findTransactionsByAccountEmailAndStatusPembayaran(@Param("email") String email, @Param("statusPembayaran") STATUS_PEMBAYARAN statusPembayaran, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND t.status = :status")
    Page<Transaction> findTransactionsByAccountEmailAndStatus(@Param("email") String email, @Param("status") Status status, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email AND t.status = :status AND t.statusPembayaran = :statusPembayaran")
    Page<Transaction> findTransactionsByAccountEmailAndStatusANDStatusPembayaran(@Param("email") String email, @Param("status") Status status, @Param("statusPembayaran") STATUS_PEMBAYARAN statusPembayaran, Pageable pageable);
}
