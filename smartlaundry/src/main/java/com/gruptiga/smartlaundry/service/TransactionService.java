package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {

    TransactionResponse createNewTransaction (TransactionRequest request, String email);

//    List<TransactionResponse> getAllTransactions();

    TransactionResponse updateStatusDone(String id, Status status);

    TransactionResponse updateStatusPembayaranDone(String id);

    TransactionResponse updateStatusPembayaranExpired(String id);

    Page<TransactionResponse> getByDateAndAccount(String date, String email, String keyword, int page, int size);

    List<TransactionResponse> getAllTransactionsBaru(SearchTransactionRequest request);
    TransactionResponse deleteById(String transactionId);

    TransactionResponse getTransactionById(String trxId);

    TransactionResponse createNewTransactionCash(TransactionRequest request, String email);

    Page<TransactionResponse> getTransactionsByEmailAndKeyword(String email, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailStatusPembayaranAndKeyword(String email, STATUS_PEMBAYARAN statusPembayaran, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailStatusAndKeyword(String email, Status status, String keyword, int page, int size);

    Page<TransactionResponse> getTransactionsByEmailStatusStatusPembayaranAndKeyword(String email, Status status, STATUS_PEMBAYARAN statusPembayaran, String keyword, int page, int size);
}
