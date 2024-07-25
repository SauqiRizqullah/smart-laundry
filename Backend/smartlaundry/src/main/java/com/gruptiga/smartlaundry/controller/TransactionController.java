package com.gruptiga.smartlaundry.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
import com.gruptiga.smartlaundry.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION)
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(produces = "application/json")
    public TransactionResponse createNewTransaction (
            @RequestBody TransactionRequest transactionRequest
    ){
        return transactionService.createNewTransaction(transactionRequest);
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



}
