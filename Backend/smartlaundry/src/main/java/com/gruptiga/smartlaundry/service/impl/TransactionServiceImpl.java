package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.constant.Detail;
import com.gruptiga.smartlaundry.constant.Payment;
import com.gruptiga.smartlaundry.constant.Status;
import com.gruptiga.smartlaundry.dto.request.TransactionRequest;
import com.gruptiga.smartlaundry.dto.response.TransactionDetailResponse;
import com.gruptiga.smartlaundry.dto.response.TransactionResponse;
import com.gruptiga.smartlaundry.entity.*;
import com.gruptiga.smartlaundry.repository.TransactionRepository;
import com.gruptiga.smartlaundry.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Long.parseLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final CustomerService customerService;

    private final AccountService accountService;

    private final TransactionDetailService transactionDetailService;

    private final ServiceTypeService typeService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse createNewTransaction(TransactionRequest request) {

        // cari objek yang sudah ada

        Customer customer = customerService.getById(request.getCustomerId());

        Account account = accountService.getById(request.getAccountId());

        // Buat objek transaksi

        Transaction trx = Transaction.builder()
                .customer(customer)
                .account(account)
                .status(Status.ONGOING)
                .totalPrice(parseLong("0"))
                .payment(Payment.valueOf(request.getPayment()))
                .orderDate(new Date())
                .build();

        // Simpan transaksi di database

        Transaction savedTransaction = transactionRepository.save(trx);

        // Membuat daftar detail dari transaksi

        List<TransactionDetail> trxDetail = request.getTransactionDetails().stream()
                .map(detailRequest -> {

                    // Informasi quantity pada terminal Intellij
                    log.info("Kuantitas dalam order ini sejumlah  : {}", detailRequest.getQty());

                    // Pemilihan pelayanan laundry

                    ServiceType serviceType = typeService.getById(detailRequest.getServiceTypeId());

                    // Mengembalikan nilai objek transaksi detail

                    return TransactionDetail.builder()
                            .trx(savedTransaction)
                            .serviceType(serviceType)
                            .qty(detailRequest.getQty())
                            .price(serviceType.getPrice() * detailRequest.getQty())
                            .build();

                }).toList();

        // simpan transaksi detail di database

        transactionDetailService.createBulk(trxDetail);
        savedTransaction.setTransactionDetailList(trxDetail);

        // Menjumlahkan harga total

        AtomicLong totalPrices = new AtomicLong();

        // Logic untuk mendapatkan Array yang berisi harga setiap transaksi detail

        trxDetail.forEach(detail -> totalPrices.addAndGet(detail.getPrice()));

        // Membuat respons dari transaksi detail dengan dikumpulin di metode stream map

        List<TransactionDetailResponse> trxDetailResponse = trxDetail.stream().map(
                detail -> {
                    return TransactionDetailResponse.builder()
                            .trxDetailId(detail.getTrxDetailId())
                            .serviceTypeId(detail.getServiceType().getServiceTypeId())
                            .qty(detail.getQty())
                            .price(detail.getPrice())
                            .build();
                }
        ).toList();

        return TransactionResponse.builder()
                .trxId(savedTransaction.getTrxId())
                .customerId(savedTransaction.getCustomer().getCustomerId())
                .accountId(savedTransaction.getAccount().getAccountId())
                .status(savedTransaction.getStatus().toString())
                .transactionDetailList(savedTransaction.getTransactionDetailList())
                .totalPrice(totalPrices.get())
                .payment(savedTransaction.getPayment().toString())
                .orderDate(savedTransaction.getOrderDate())
                .build();
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {

        // Memanggil repository findAll terlebih dahulu
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(trx -> {
            List<TransactionDetailResponse> trxDetailResponse = trx.getTransactionDetailList().stream().map(trxDetail -> {
                return TransactionDetailResponse.builder()
                        .trxDetailId(trxDetail.getTrxDetailId())
                        .serviceTypeId(trxDetail.getServiceType().getServiceTypeId())
                        .qty(trxDetail.getQty())
                        .price(trxDetail.getPrice())
                        .build();
            }).toList();

            AtomicLong totalPrices = new AtomicLong();

            // Logic untuk mendapatkan Array yang berisi harga setiap transaksi detail

            trxDetailResponse.forEach(detail -> totalPrices.addAndGet(detail.getPrice()));

            return TransactionResponse.builder()
                    .trxId(trx.getTrxId())
                    .customerId(trx.getCustomer().getCustomerId())
                    .accountId(trx.getAccount().getAccountId())
                    .status(trx.getStatus().toString())
                    .transactionDetailList(trx.getTransactionDetailList())
                    .totalPrice(totalPrices.get())
                    .payment(trx.getPayment().toString())
                    .orderDate(trx.getOrderDate())
                    .build();
        }).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse updateStatusDone(String id) {
        Transaction trx = transactionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id transaksi tidak ditemukan!!!"));

        trx.setStatus(Status.DONE);

//        transactionRepository.updateStatusById(id, newStatus);
        transactionRepository.save(trx);

        return parseTransactionToTransactionResponse(trx);
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
                .customerId(trx.getCustomer().getCustomerId())
                .accountId(trx.getAccount().getAccountId())
                .status(trx.getStatus().toString())
                .transactionDetailList(trx.getTransactionDetailList())
                .totalPrice(trx.getTotalPrice())
                .payment(trx.getPayment().toString())
                .orderDate(trx.getOrderDate())
                .build();
        // buat get by tanggal dan akun untuk transaksi
    }
}
