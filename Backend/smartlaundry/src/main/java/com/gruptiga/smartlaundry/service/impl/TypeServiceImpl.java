package com.gruptiga.smartlaundry.service.impl;


import com.gruptiga.smartlaundry.dto.request.TypeRequest;
import com.gruptiga.smartlaundry.dto.response.TypeResponse;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.Type;
import com.gruptiga.smartlaundry.repository.TypeRepository;
import com.gruptiga.smartlaundry.service.AccountService;
import com.gruptiga.smartlaundry.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final AccountService accountService;

    @Override
    public TypeResponse createType(TypeRequest request) {
        Account account = accountService.getByEmail(request.getEmailAccount());

        Type type = Type.builder()
                .name(request.getName())
                .account(account)
                .build();

        typeRepository.saveAndFlush(type);

        return parseTypeResponse(type);
    }

    @Override
    public Type getById(String typeId) {
        return typeRepository.findById(typeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Type tidak ditemukan!!!"));
    }

    @Override
    public List<TypeResponse> getAllType(TypeRequest request) {
        return List.of();
    }

    @Override
    public TypeResponse updateType(Type type) {
        typeRepository.findById(type.getTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type ID not found"));

        typeRepository.saveAndFlush(type);

        return parseTypeResponse(type);
    }

    @Override
    public TypeResponse deleteById(String typeId) {
        Type type = typeRepository.findById(typeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Type tidak ditemukan!!!"));
        typeRepository.delete(type);
        return parseTypeResponse(type);
    }

    @Override
    public long count() {
        return 0;
    }


    private TypeResponse parseTypeResponse(Type type) {
        String id = type.getTypeId() == null ? null : type.getTypeId();

        return TypeResponse.builder()
                .typeId(id)
                .name(type.getName())
                .build();
    }
}
