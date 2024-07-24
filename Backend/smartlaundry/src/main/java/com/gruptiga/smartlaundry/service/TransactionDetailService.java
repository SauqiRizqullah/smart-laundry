package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetail);
}
