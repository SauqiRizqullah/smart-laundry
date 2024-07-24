package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.entity.TransactionDetail;
import com.gruptiga.smartlaundry.repository.TransactionDetailRepository;
import com.gruptiga.smartlaundry.service.TransactionDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDetailServiceImpl implements TransactionDetailService {

    private final TransactionDetailRepository transactionDetailRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetail) {
        return transactionDetailRepository.saveAllAndFlush(transactionDetail);
    }
}
