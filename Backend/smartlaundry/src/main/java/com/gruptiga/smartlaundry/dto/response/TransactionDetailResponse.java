package com.gruptiga.smartlaundry.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionDetailResponse {
    private String trxDetailId;
    private String serviceTypeId;
    private Integer qty;
    private Long price;

}
