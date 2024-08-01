package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.request.TypeRequest;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.dto.response.ServicesResponse;
import com.gruptiga.smartlaundry.dto.response.TypeResponse;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.entity.Type;
import com.gruptiga.smartlaundry.service.ServiceServices;
import com.gruptiga.smartlaundry.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TYPE)
public class TypeController {

    private final TypeService typeService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<TypeResponse>> createNewService (
            @RequestBody TypeRequest typeRequest
    ){
        TypeResponse newService = typeService.createType(typeRequest);

        CommonResponse<TypeResponse> response = CommonResponse.<TypeResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Service baru telah dibuat!!!")
                .data(newService)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = APIUrl.PATH_VAR_TYPE_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<Type>> getById (
            @PathVariable String typesId
    ) {
        Type type = typeService.getById(typesId);
        CommonResponse<Type> response = CommonResponse.<Type>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Customer berhasil didapatkan!!!")
                .data(type)
                .build();
        return ResponseEntity.ok(response);
    }

//    @GetMapping(produces = "application/json")
//    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers (
//            @RequestParam(name = "name", required = false) String name
//    ) {
//        SearchCustomerRequest customerRequest = SearchCustomerRequest.builder()
//                .customerName(name)
//                .build();
//
//        List<CustomerResponse> allCustomers = customerService.getAllCustomers(customerRequest);
//
//        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("Semua data Customer berhasil didapatkan!!!")
//                .data(allCustomers)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }

    @PutMapping
    public ResponseEntity<CommonResponse<TypeResponse>> updateServices(
            @RequestBody TypeRequest typeRequest
    ) {

        Type type1 = typeService.getById(typeRequest.getTypeId());
        TypeResponse updatedService = typeService.updateType(type1 , typeRequest);

        CommonResponse<TypeResponse> response = CommonResponse.<TypeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Service " + typeRequest.getTypeId() + " telah diperbarui!!!")
                .data(updatedService)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_TYPE_ID,produces = "application/json")
    public ResponseEntity<CommonResponse<TypeResponse>> deleteById (
            @PathVariable String typesId
    ) {
        TypeResponse typeResponse = typeService.deleteById(typesId);

        CommonResponse<TypeResponse> response = CommonResponse.<TypeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Service " + typesId + " telah dihapus")
                .data(typeResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}