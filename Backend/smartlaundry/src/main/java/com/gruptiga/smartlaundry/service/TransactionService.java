package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    TransactionResponse createNewTransaction (TransactionRequest request, String email);

    List<TransactionResponse> getAllTransactions();

    TransactionResponse updateStatusDone(String id);

    List<TransactionResponse> getByDateAndAccount(String date, String accountId);

    List<TransactionResponse> getAllTransactionsBaru(SearchTransactionRequest request);
}
