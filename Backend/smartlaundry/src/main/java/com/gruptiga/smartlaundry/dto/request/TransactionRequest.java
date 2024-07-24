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

    private String accountId;

    private List<TransactionDetailRequest> transactionDetails;

    private String payment;

}
