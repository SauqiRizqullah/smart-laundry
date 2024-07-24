package com.gruptiga.smartlaundry.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class TransactionResponse {
    private String trxId;

    private String customerId;

    private String serviceTypeId;

    private String status;

    private Integer qty;

    private Long totalPrice;

    private String payment;

    private LocalDate orderDate;
}
