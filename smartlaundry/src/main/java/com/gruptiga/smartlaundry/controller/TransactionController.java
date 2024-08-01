package com.gruptiga.smartlaundry.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.dto.request.StatusUpdateRequest;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.CommonResponse;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
import com.gruptiga.smartlaundry.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<TransactionResponse> createNewTransaction(
            @RequestBody TransactionRequest transactionRequest,
            @RequestParam String email) {

        TransactionResponse response;
        if ("CASH".equalsIgnoreCase(transactionRequest.getPayment())) {
            response = transactionService.createNewTransactionCash(transactionRequest, email);
        } else {
            response = transactionService.createNewTransaction(transactionRequest, email);
        }
        return ResponseEntity.ok(response);
    }


    @PutMapping(path = APIUrl.PATH_VAR_TRANSACTION_ID, produces = "application/json")
    public ResponseEntity<TransactionResponse> updateStatusDoneById(
            @PathVariable String trxId,
            @RequestBody StatusUpdateRequest statusUpdateRequest) {
        Status status;
        try {
            status = Status.valueOf(statusUpdateRequest.getStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }

        TransactionResponse updatedTransactionResponse = transactionService.updateStatusDone(trxId, status);
        return ResponseEntity.ok(updatedTransactionResponse);
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
    public ResponseEntity<Page<TransactionResponse>> getByDateAndAccount(
            @RequestParam(name = "date") String date,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        LocalDate orderDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        Page<TransactionResponse> transactions = transactionService.getByDateAndAccount(orderDate.toString(), email, keyword, page, size);

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

    @GetMapping(APIUrl.TRANSACTION_ACCOUNT)
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByEmail(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TransactionResponse> transactions = transactionService.getTransactionsByEmailAndKeyword(email, keyword, page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(APIUrl.BYSTATUSPembayaran_ACCOUNT)
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByEmailAndStatusPembayaran(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "statusPembayaran") STATUS_PEMBAYARAN statusPembayaran,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TransactionResponse> transactions = transactionService.getTransactionsByEmailStatusPembayaranAndKeyword(email, statusPembayaran, keyword, page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(APIUrl.BYSTATUS_ACCOUNT)
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByEmailAndStatus(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "status") Status status,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TransactionResponse> transactions = transactionService.getTransactionsByEmailStatusAndKeyword(email, status, keyword, page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(APIUrl.BYSTATUS_Pembayaran_ACCOUNT)
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByEmailStatusAndStatusPembayaran(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "status") Status status,
            @RequestParam(name = "statusPembayaran") STATUS_PEMBAYARAN statusPembayaran,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<TransactionResponse> transactions = transactionService.getTransactionsByEmailStatusStatusPembayaranAndKeyword(email, status, statusPembayaran, keyword, page, size);
        return ResponseEntity.ok(transactions);
    }
}
