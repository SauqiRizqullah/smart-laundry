package com.gruptiga.smartlaundry.dto.response;


import com.gruptiga.smartlaundry.entity.ServiceType;
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

    private String accountId;

    private String trxId;

    private String customerId;

    private ServiceType serviceType;

    private String status;

    private String statusPembayaran;

    private Integer qty;

    private Long totalPrice;

    private String payment;

    private LocalDate orderDate;
    private String paymentUrl;
}
