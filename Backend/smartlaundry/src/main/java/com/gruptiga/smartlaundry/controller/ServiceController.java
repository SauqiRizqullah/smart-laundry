package com.gruptiga.smartlaundry.controller;


import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.ServiceRequest;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.dto.response.ServicesResponse;
import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.service.ServiceServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.SERVICE)
public class ServiceController {

    private final ServiceServices serviceServices;

    @PostMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<ServicesResponse>> createNewService (
            @RequestBody ServiceRequest serviceRequest
    ){
        ServicesResponse newService = serviceServices.createService(serviceRequest);

        CommonResponse<ServicesResponse> response = CommonResponse.<ServicesResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Service baru telah dibuat!!!")
                .data(newService)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = APIUrl.PATH_VAR_SERVICE_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<Service>> getById (
            @PathVariable String serviceId
    ) {
        Service service = serviceServices.getById(serviceId);
        CommonResponse<Service> response = CommonResponse.<Service>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Customer berhasil didapatkan!!!")
                .data(service)
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
    public ResponseEntity<CommonResponse<ServicesResponse>> updateServices(
            @RequestBody ServiceRequest service
    ) {

        Service service1 = serviceServices.getById(service.getServiceId());
        ServicesResponse updatedService = serviceServices.updateCustomer(service1);

        CommonResponse<ServicesResponse> response = CommonResponse.<ServicesResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Service " + service.getName() + " telah diperbarui!!!")
                .data(updatedService)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_SERVICE_ID,produces = "application/json")
    public ResponseEntity<CommonResponse<ServicesResponse>> deleteById (
            @PathVariable String serviceId
    ) {
        ServicesResponse servicesResponse = serviceServices.deleteById(serviceId);

        CommonResponse<ServicesResponse> response = CommonResponse.<ServicesResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Service " + serviceId + " telah dihapus")
                .data(servicesResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
