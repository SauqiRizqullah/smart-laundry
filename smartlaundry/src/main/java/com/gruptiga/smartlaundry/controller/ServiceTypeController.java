package com.gruptiga.smartlaundry.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.SearchServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.TypeRequest;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.ServiceTypeResponse;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.entity.ServiceType;
import com.gruptiga.smartlaundry.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.SERVICETYPE)
public class ServiceTypeController {
    private final ServiceTypeService typeService;

    private final ObjectMapper objectMapper;

//    @PostMapping(produces = "application/json")
//    public ResponseEntity<CommonResponse<ServiceTypeResponse>> createServiceType (
//            @RequestBody ServiceTypeRequest request
//            ){
//        ServiceTypeResponse serviceType = typeService.createServiceType(request);
//
//        CommonResponse<ServiceTypeResponse> response= CommonResponse.<ServiceTypeResponse>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message("Data harga pelayanan baru telah dibuat!!!")
//                .data(serviceType)
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<ServiceTypeResponse>> createServiceType (
            @RequestBody ServiceTypeRequest request
            ){
        ServiceTypeResponse serviceType = typeService.createServiceType(request);

        CommonResponse<ServiceTypeResponse> response = CommonResponse.<ServiceTypeResponse>
                builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data pelayanan baru pada laundry berhasil dibuat!!!")
                .data(serviceType)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = APIUrl.PATH_VAR_SERVICETYPE_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<ServiceType>> getById (
            @PathVariable String serviceTypeId
    ) {
        ServiceType serviceType = typeService.getById(serviceTypeId);

        CommonResponse<ServiceType> response = CommonResponse.<ServiceType>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Service Type berhasil didapatkan!!!")
                .data(serviceType)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<List<ServiceTypeResponse>>> getAll (
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "service", required = false) String service
    ) {
        SearchServiceTypeRequest typeRequest = SearchServiceTypeRequest.builder()
                .type(type)
                .service(service)
                .build();

        List<ServiceTypeResponse> allServices = typeService.getAllServicesType(typeRequest);

        CommonResponse<List<ServiceTypeResponse>> response = CommonResponse.<List<ServiceTypeResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Semua data pelayanan laundry berhasil didapatkan")
                .data(allServices)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_SERVICETYPE_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<ServiceTypeResponse>> deleteById (
            @PathVariable String serviceTypeId
    ) {
        ServiceTypeResponse typeResponse = typeService.deleteById(serviceTypeId);

        CommonResponse<ServiceTypeResponse> response = CommonResponse.<ServiceTypeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data pelayanan laundry " + serviceTypeId + " telah dihapus!!!")
                .data(typeResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<ServiceTypeResponse>> updateCustomer (
            @RequestBody ServiceTypeRequest serviceType1
    ){
        ServiceType serviceType = typeService.getById(serviceType1.getServiceTypeId());
        ServiceTypeResponse updatedServiceType = typeService.updateServiceType(serviceType, serviceType1);

        CommonResponse<ServiceTypeResponse> response = CommonResponse.<ServiceTypeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Service Type " + serviceType.getServiceTypeId() + " telah diperbarui!!!")
                .data(updatedServiceType)
                .build();

        return ResponseEntity.ok(response);
    }
}
