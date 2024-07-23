package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.CustomerRequest;
import com.gruptiga.smartlaundry.dto.request.SearchCustomerRequest;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.entity.Customer;
import com.gruptiga.smartlaundry.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.CUSTOMER)
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<CustomerResponse>> createNewCustomer (
            @RequestBody CustomerRequest customerRequest
    ){
        CustomerResponse newCustomer = customerService.createCustomer(customerRequest);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Customer baru telah dibuat!!!")
                .data(newCustomer)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(path = APIUrl.PATH_VAR_CUSTOMER_ID, produces = "application/json")
    public ResponseEntity<CommonResponse<Customer>> getById (
            @PathVariable String customerId
    ) {
        Customer customer = customerService.getById(customerId);
        CommonResponse<Customer> response = CommonResponse.<Customer>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Data Customer berhasil didapatkan!!!")
                .data(customer)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAllCustomers (
            @RequestParam(name = "name", required = false) String name
    ) {
        SearchCustomerRequest customerRequest = SearchCustomerRequest.builder()
                .customerName(name)
                .build();

        List<CustomerResponse> allCustomers = customerService.getAllCustomers(customerRequest);

        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Semua data Customer berhasil didapatkan!!!")
                .data(allCustomers)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<CommonResponse<CustomerResponse>> updateCustomer (
            @RequestBody Customer customer
    ){
        CustomerResponse updatedCustomer = customerService.updateCustomer(customer);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Customer " + customer.getCustomerId() + " telah diperbarui!!!")
                .data(updatedCustomer)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_CUSTOMER_ID,produces = "application/json")
    public ResponseEntity<CommonResponse<CustomerResponse>> deleteById (
            @PathVariable String customerId
    ) {
        CustomerResponse customerResponse = customerService.deleteById(customerId);

        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Customer " + customerId + " telah dihapus")
                .data(customerResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
