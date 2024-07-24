package com.gruptiga.smartlaundry.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    private String customerId;

    private String serviceTypeId;

    private Integer qty;

    private String payment;

}
