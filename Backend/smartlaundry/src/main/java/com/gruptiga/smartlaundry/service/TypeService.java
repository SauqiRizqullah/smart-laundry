package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.TypeRequest;
import com.gruptiga.smartlaundry.dto.response.TypeResponse;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.Type;

import java.util.List;

public interface TypeService {
    TypeResponse createType(TypeRequest request);

    Type getById(String typeId);

    List<TypeResponse> getAllType (TypeRequest request);

    TypeResponse updateType (Type type, TypeRequest typeRequest);

    TypeResponse deleteById(String typeId);

    long count();
}
