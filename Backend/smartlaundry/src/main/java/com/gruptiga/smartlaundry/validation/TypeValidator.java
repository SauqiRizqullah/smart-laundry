//package com.gruptiga.smartlaundry.validation;
//
//
//import com.gruptiga.smartlaundry.constant.Detail;
//import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
//import com.gruptiga.smartlaundry.dto.request.TypeRequest;
//import com.gruptiga.smartlaundry.entity.Account;
//import com.gruptiga.smartlaundry.entity.Service;
//import com.gruptiga.smartlaundry.entity.ServiceType;
//import com.gruptiga.smartlaundry.entity.Type;
//import com.gruptiga.smartlaundry.repository.ServiceTypeRepository;
//import com.gruptiga.smartlaundry.repository.TypeRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.regex.Pattern;
//
//@Component
//public class TypeValidator {
//
//    @Autowired
//    private TypeRepository typeRepository;
//
//
//    public void validateCreateTypeRequest(TypeRequest typeRequest,Type type, Account account) {
//        if (typeRequest.getName() == null || typeRequest.getName().trim().isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request cannot be null");
//        }
//
//        boolean exists = typeRepository.existsByTypeAndAccount(
//                type,
//                account
//        );
//        if (exists) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ServiceType with the same type, service, detail, and account already exists");
//        }
//    }
//
//    public void validateUpdateTypeRequest(ServiceType request) {
//    }
//
//}
//
