package com.gruptiga.smartlaundry.specification;

import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.entity.ServiceType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeSpecification {
    public static Specification<ServiceType> getSpecification (SearchServiceTypeRequest request){
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getType() != null) {
                Predicate typePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%" + request.getType().toLowerCase() + "%");
                predicates.add(typePredicate);
            } if (request.getService() != null) {
                Predicate servicePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("service")), "%" + request.getService().toLowerCase() + "%");
                predicates.add(servicePredicate);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
