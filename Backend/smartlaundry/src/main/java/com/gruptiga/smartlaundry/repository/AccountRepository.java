package com.gruptiga.smartlaundry.repository;

import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.entity.Transaction;
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

    @Query("SELECT c FROM Customer c WHERE c.account.email = :email")
    List<Customer> findCustomersByAccountEmail(@Param("email") String email);

    @Query("SELECT s FROM ServiceType s WHERE s.account.email = :email")
    List<ServiceType> findServiceTypesByAccountEmail(@Param("email") String email);

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

    @Query("SELECT t FROM Transaction t WHERE t.account.email = :email")
    List<Transaction> findTransactionsByAccountEmail(@Param("email") String email);
}
