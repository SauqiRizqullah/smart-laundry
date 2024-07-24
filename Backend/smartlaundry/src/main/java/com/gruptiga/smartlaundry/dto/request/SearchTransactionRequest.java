package com.gruptiga.smartlaundry.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchTransactionRequest {
    private String orderDate;

    private String status;

    private String minDate;

    private String maxDate;
}
