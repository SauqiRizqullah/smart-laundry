package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
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

    @GetMapping(produces = "application/json")
    public List<TransactionResponse> getAllTransactions (){
        return transactionService.getAllTransactions();
    }

    @PutMapping(path = APIUrl.PATH_VAR_TRANSACTION_ID, produces = "application/json")
    public TransactionResponse updateStatusDoneById (
            @PathVariable String trxId
    ){
        return transactionService.updateStatusDone(trxId);
    }

}
