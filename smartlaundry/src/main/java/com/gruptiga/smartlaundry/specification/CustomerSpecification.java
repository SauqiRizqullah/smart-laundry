package com.gruptiga.smartlaundry.specification;

import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification (SearchCustomerRequest request){
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getCustomerName() != null) {
                Predicate customerNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getCustomerName().toLowerCase() + "%");
                predicates.add(customerNamePredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
