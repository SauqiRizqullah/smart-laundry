package com.gruptiga.smartlaundry.specification;

import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Transaction;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecifications {
    public static Specification<Transaction> getSpecification (SearchTransactionRequest request){
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getOrderDate() != null) {
                request.setMinDate(null);
                request.setMaxDate(null);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//                    Date orderDate = formatter.parse(request.getOrderDate());
                    LocalDate localDate = LocalDate.parse(request.getOrderDate(), formatter);
                    Predicate orderDatePredicate = criteriaBuilder.equal(root.get("orderDate"), localDate);
                    predicates.add(orderDatePredicate);

            } if (request.getStatus() != null) {
                Predicate  statusPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), "%" + request.getStatus().toLowerCase() + "%");
                predicates.add(statusPredicate);
            }
            if (request.getMinDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate minDate = LocalDate.parse(request.getMinDate(), formatter);
                Predicate minDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), minDate);
                predicates.add(minDatePredicate);
            }

            if (request.getMaxDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate maxDate = LocalDate.parse(request.getMaxDate(), formatter);
                Predicate maxDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("orderDate"), maxDate);
                predicates.add(maxDatePredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();

            // PR

            // 1. Ubah Entity hingga Controller (service_type dan t_transactions)
            // 2. Uji Postman
            // 3. Filtering max dan min date
            // 4. Validation
        };
    }
}
