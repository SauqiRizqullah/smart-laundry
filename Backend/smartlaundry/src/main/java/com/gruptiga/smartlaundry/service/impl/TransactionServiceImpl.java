package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Payment;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createNewTransaction(TransactionRequest request, String email) {

        transactionValidator.validateCreateTransactionRequest(request);

        // cari objek yang sudah ada

        Account accountss = accountService.getByEmail(email);


        // Convert Tanggal
        Date dateNow = new Date();

        Instant instant = dateNow.toInstant();

        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Buat objek transaksi

        Transaction trx = Transaction.builder()
                .account(accountss)
                .customerId(request.getCustomersId())
                .serviceTypeId(request.getServiceTypeId())
                .status(Status.ANTRIAN)
                .qty(request.getQty())
                .totalPrice(Long.valueOf(request.getServicePrice() * request.getQty()))
                .payment(Payment.valueOf(request.getPayment()))
                .orderDate(localDate)
                .build();


        // Simpan transaksi di database

        Transaction savedTransaction = transactionRepository.save(trx);

        return TransactionResponse.builder()
                .accountId(savedTransaction.getAccount().getAccountId())
                .trxId(savedTransaction.getTrxId())
                .customerId(savedTransaction.getCustomerId())
                .serviceTypeId(savedTransaction.getServiceTypeId())
                .status(savedTransaction.getStatus().toString())
                .qty(savedTransaction.getQty())
                .totalPrice(savedTransaction.getTotalPrice())
                .payment(savedTransaction.getPayment().toString())
                .orderDate(savedTransaction.getOrderDate())
                .build();
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        // Memanggil repository findAll terlebih dahulu
        List<Transaction> transactions = transactionRepository.findAll();

//        return transactions.stream().map(trx -> {
//            List<TransactionDetailResponse> trxDetailResponse = trx.getTransactionDetailList().stream().map(trxDetail -> {
//                return TransactionDetailResponse.builder()
//                        .trxDetailId(trxDetail.getTrxDetailId())
//                        .serviceTypeId(trxDetail.getServiceType().getServiceTypeId())
//                        .qty(trxDetail.getQty())
//                        .price(trxDetail.getPrice())
//                        .build();
//            }).toList();
//
//            AtomicLong totalPrices = new AtomicLong();
//
//            // Logic untuk mendapatkan Array yang berisi harga setiap transaksi detail
//
//            trxDetailResponse.forEach(detail -> totalPrices.addAndGet(detail.getPrice()));
//
//            return TransactionResponse.builder()
//                    .trxId(trx.getTrxId())
//                    .customerId(trx.getCustomer().getCustomerId())
//                    .accountId(trx.getAccount().getAccountId())
//                    .status(trx.getStatus().toString())
//                    .transactionDetailList(trx.getTransactionDetailList())
//                    .totalPrice(totalPrices.get())
//                    .payment(trx.getPayment().toString())
//                    .orderDate(trx.getOrderDate())
//                    .build();
//        }).toList();

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
    public TransactionResponse updateStatusDone(String id) {

        Transaction trx = transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id transaksi tidak ditemukan!!!"));

        trx.setStatus(Status.SELESAI);

//        transactionRepository.updateStatusById(id, newStatus);
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

    private TransactionResponse parseTransactionToTransactionResponse(Transaction trx) {
        String id;
        if (trx.getTrxId() == null){
            id = null;
        } else {
            id = trx.getTrxId();
        }

        return TransactionResponse.builder()
                .trxId(id)
                .status(trx.getStatus().toString())
                .qty(trx.getQty())
                .totalPrice(trx.getTotalPrice())
                .payment(trx.getPayment().toString())
                .orderDate(trx.getOrderDate())
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
                .build();
    }
}
