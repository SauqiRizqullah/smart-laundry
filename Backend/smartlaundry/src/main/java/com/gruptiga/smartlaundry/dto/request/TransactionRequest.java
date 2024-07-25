package com.gruptiga.smartlaundry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    @NotBlank(message = "Service Price wajib diisi!!!")
    private Integer servicePrice;

    @NotBlank(message = "Service Type ID wajib diisi!!!")
    private String serviceTypeId;

    @NotBlank(message = "Customers ID wajib diisi!!!")
    private String customersId;

    @NotBlank(message = "Qty wajib diisi!!!")
    private Integer qty;

    @NotBlank(message = "Payment wajib diisi!!!")
    private String payment;

}
