package com.gruptiga.smartlaundry.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
import com.gruptiga.smartlaundry.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(produces = "application/json")
    public TransactionResponse createNewTransaction (
            @RequestBody TransactionRequest transactionRequest,
            @RequestParam String email
    ){
        return transactionService.createNewTransaction(transactionRequest, email);
    }

//    @GetMapping(produces = "application/json")
//    public List<TransactionResponse> getAllTransactions (){
//        return transactionService.getAllTransactions();
//    }

    @PutMapping(path = APIUrl.PATH_VAR_TRANSACTION_ID, produces = "application/json")
    public TransactionResponse updateStatusDoneById (
            @PathVariable String trxId
    ){
        return transactionService.updateStatusDone(trxId);
    }

    @GetMapping(produces = "application/json")
    public List<TransactionResponse> getAllTransactionsBaru (
            @RequestParam(name = "orderDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String orderDate,
            @RequestParam(name = "status",required = false) String status,
            @RequestParam(name = "minDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String minDate,
            @RequestParam(name = "maxDate", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String maxDate
    )
    {
        SearchTransactionRequest searchTransactionRequest = SearchTransactionRequest.builder()
                .orderDate(orderDate)
                .status(status)
                .minDate(minDate)
                .maxDate(maxDate)
                .build();
        return transactionService.getAllTransactionsBaru(searchTransactionRequest);

//        return transactionService.getAllTransactions();
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_TRANSACTION_ID,produces = "application/json")
    public ResponseEntity<CommonResponse<TransactionResponse>> deleteById (
            @PathVariable String trxId
    ) {
        TransactionResponse transactionResponse = transactionService.deleteById(trxId);

        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Data Transaction " + trxId + " telah dihapus")
                .data(transactionResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(APIUrl.BY_DATE_ACCOUNT)
    public ResponseEntity<List<TransactionResponse>> getByDateAndAccount(
            @RequestParam(name = "date") String date,
            @RequestParam(name = "email") String email) {

        LocalDate orderDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        List<TransactionResponse> transactions = transactionService.getByDateAndAccount(orderDate.toString(), email);

        return ResponseEntity.ok(transactions);
    }


    @GetMapping(APIUrl.PATH_VAR_TRANSACTION_ID)
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable("trxId") String trxId) {

        TransactionResponse transactionResponse = transactionService.getTransactionById(trxId);

        if (transactionResponse == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transactionResponse);
    }
}
