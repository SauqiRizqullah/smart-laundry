package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Payment;
import com.gruptiga.smartlaundry.constant.STATUS_PEMBAYARAN;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.SearchTransactionRequest;
import com.gruptiga.smartlaundry.dto.request.ServiceTypeRequest;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.CustomerResponse;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
import com.gruptiga.smartlaundry.entity.*;
import com.gruptiga.smartlaundry.repository.TransactionRepository;
import com.gruptiga.smartlaundry.service.*;
import com.gruptiga.smartlaundry.specification.TransactionSpecifications;
import com.gruptiga.smartlaundry.validation.TransactionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    @Autowired
    private TransactionValidator transactionValidator;

    private final MidtransService midtransService;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createNewTransaction(TransactionRequest request, String email) {

        transactionValidator.validateCreateTransactionRequest(request);

        // Fetch existing account
        Account account = accountService.getByEmail(email);

        // Convert Date
        Date dateNow = new Date();
        Instant instant = dateNow.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Create transaction object
        Transaction trx = Transaction.builder()
                .account(account)
                .customerId(request.getCustomersId())
                .serviceTypeId(request.getServiceTypeId())
                .status(Status.ANTRIAN)
                .qty(request.getQty())
                .totalPrice(Long.valueOf(request.getServicePrice() * request.getQty()))
                .payment(Payment.valueOf(request.getPayment()))
                .orderDate(localDate)
                .statusPembayaran(STATUS_PEMBAYARAN.BELUM_DIBAYAR)
                .build();

        // Save transaction to database
        Transaction savedTransaction = transactionRepository.save(trx);

        // Prepare Midtrans transaction request
        Map<String, Object> transactionRequest = new HashMap<>();
        transactionRequest.put("payment_type", request.getPayment());
        transactionRequest.put("transaction_details", Map.of(
                "order_id", savedTransaction.getTrxId(),
                "gross_amount", savedTransaction.getTotalPrice()
        ));
        transactionRequest.put("customer_details", Map.of(
                "first_name", request.getCustomersId(),
                 "email", savedTransaction.getAccount().getEmail(),
                 "phone", savedTransaction.getAccount().getContact()
        ));
        transactionRequest.put("custom_expiry", Map.of(
                "expiry_duration", 60,
                "unit", "minute"
        ));

        // Create Midtrans transaction
        try {
            Map<String, Object> transactionResult = midtransService.createTransaction(transactionRequest);

            if ("201".equals(transactionResult.get("status_code"))) {
                // Update transaction status and save
                trx.setStatus(Status.ANTRIAN);
                transactionRepository.save(trx);

                // Extract QR code URL
                List<Map<String, Object>> actions = (List<Map<String, Object>>) transactionResult.get("actions");
                if (actions != null && !actions.isEmpty()) {
                    Map<String, Object> action = actions.get(0);
                    String qrCodeUrl = (String) action.get("url");

                    // Build and return response with QR code URL
                    return TransactionResponse.builder()
                            .accountId(savedTransaction.getAccount().getAccountId())
                            .trxId(savedTransaction.getTrxId())
                            .customerId(savedTransaction.getCustomerId())
                            .serviceTypeId(savedTransaction.getServiceTypeId())
                            .status(trx.getStatus().toString())
                            .qty(savedTransaction.getQty())
                            .totalPrice(savedTransaction.getTotalPrice())
                            .payment(savedTransaction.getPayment().toString())
                            .orderDate(savedTransaction.getOrderDate())
                            .paymentUrl(qrCodeUrl)
                            .statusPembayaran(String.valueOf(trx.getStatusPembayaran()))
                            .build();
                } else {
                    throw new RuntimeException("No actions found in Midtrans response.");
                }
            } else {
                throw new RuntimeException("Failed to create Midtrans transaction: " + transactionResult.get("status_message"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while creating Midtrans transaction", e);
        }
    }


    @Override
    public List<TransactionResponse> getAllTransactions() {

        // Memanggil repository findAll terlebih dahulu
        List<Transaction> transactions = transactionRepository.findAll();


        return transactions.stream().map(trx ->{
            return TransactionResponse.builder()
                    .trxId(trx.getTrxId())
                    .status(trx.getStatus().toString())
                    .qty(trx.getQty())
                    .totalPrice(trx.getTotalPrice())
                    .payment(trx.getPayment().toString())
                    .orderDate(trx.getOrderDate())
                    .build();
        }).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateStatusDone(String id, Status status) {

        Transaction trx = transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id transaksi tidak ditemukan!!!"));

        trx.setStatus(status);

        transactionRepository.save(trx);

        return parseTransactionToTransactionResponse(trx);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateStatusPembayaranDone(String id) {

        Transaction trx = transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id transaksi tidak ditemukan!!!"));

        trx.setStatusPembayaran(STATUS_PEMBAYARAN.SUDAH_DIBAYAR);

        transactionRepository.save(trx);

        return parseTransactionToTransactionResponse(trx);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateStatusPembayaranExpired(String id) {

        Transaction trx = transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id transaksi tidak ditemukan!!!"));

        trx.setStatusPembayaran(STATUS_PEMBAYARAN.EXPIRED);

        transactionRepository.save(trx);

        return parseTransactionToTransactionResponse(trx);
    }

    @Override
    public List<TransactionResponse> getByDateAndAccount(String date, String email) {
        // Get account by email
        Account account = accountService.getByEmail(email);

        // Convert date string to LocalDate
        LocalDate orderDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        // Get transactions by accountId and orderDate
        List<Transaction> transactions = transactionRepository.findTransactionsByAccountIdAndOrderDate(
                account.getAccountId(), orderDate);

        // Convert Transaction entities to TransactionResponse DTOs
        return transactions.stream()
                .map(trx -> TransactionResponse.builder()
                        .accountId(trx.getAccount() != null ? trx.getAccount().getAccountId() : null) // Ensure accountId is set
                        .trxId(trx.getTrxId())
                        .customerId(trx.getCustomerId())
                        .serviceTypeId(trx.getServiceTypeId())
                        .status(trx.getStatus().toString())
                        .qty(trx.getQty())
                        .totalPrice(trx.getTotalPrice())
                        .payment(trx.getPayment().toString())
                        .orderDate(trx.getOrderDate())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public List<TransactionResponse> getAllTransactionsBaru(SearchTransactionRequest request) {
        Specification<Transaction> specification = TransactionSpecifications.getSpecification(request);
        if(request.getOrderDate() == null && request.getStatus() == null){
            return transactionRepository.findAll().stream().map(this::parseTransactionToTransactionResponse).toList();
        } else {
            return transactionRepository.findAll(specification).stream().map(
                    this::parseTransactionToTransactionResponse
            ).toList();
        }

    }

    private TransactionResponse parseTransactionToTransactionResponse(Transaction transaction) {
        String id;
        if (transaction.getTrxId() == null){
            id = null;
        } else {
            id = transaction.getTrxId();
        }

        return TransactionResponse.builder()
                .accountId(transaction.getAccount() != null ? transaction.getAccount().getAccountId() : null)
                .trxId(transaction.getTrxId())
                .customerId(transaction.getCustomerId())
                .serviceTypeId(transaction.getServiceTypeId())
                .status(transaction.getStatus().toString())
                .qty(transaction.getQty())
                .totalPrice(transaction.getTotalPrice())
                .payment(transaction.getPayment().toString())
                .orderDate(transaction.getOrderDate())
                .statusPembayaran(String.valueOf(transaction.getStatusPembayaran()))
                .build();
        // buat get by tanggal dan akun untuk transaksi

    }

    @Override
    public TransactionResponse deleteById(String trxId) {
        Transaction transaction = transactionRepository.findById(trxId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Transaction tidak ditemukan!!!"));
        transactionRepository.delete(transaction);
        return parseTransactionToTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(String trxId) {
        Transaction transaction = transactionRepository.findById(trxId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        return TransactionResponse.builder()
                .accountId(transaction.getAccount() != null ? transaction.getAccount().getAccountId() : null)
                .trxId(transaction.getTrxId())
                .customerId(transaction.getCustomerId())
                .serviceTypeId(transaction.getServiceTypeId())
                .status(transaction.getStatus().toString())
                .qty(transaction.getQty())
                .totalPrice(transaction.getTotalPrice())
                .payment(transaction.getPayment().toString())
                .orderDate(transaction.getOrderDate())
                .statusPembayaran(String.valueOf(transaction.getStatusPembayaran()))
                .build();
    }
}
