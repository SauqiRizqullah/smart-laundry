package com.gruptiga.smartlaundry.dto.response;

import com.gruptiga.smartlaundry.entity.TransactionDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class TransactionResponse {
    private String trxId;

    private String customerId;

    private String accountId;

    private String status;

    private List<TransactionDetail> transactionDetailList;

    private Long totalPrice;

    private String payment;

    private Date orderDate;
}
