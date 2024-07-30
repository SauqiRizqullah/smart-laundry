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

    @NotBlank(message = "Service  ID wajib diisi!!!")
    private String serviceId;

    @NotBlank(message = "Service  ID wajib diisi!!!")
    private String typeId;

    @NotBlank(message = "Customers ID wajib diisi!!!")
    private String customersId;

    @NotBlank(message = "Qty wajib diisi!!!")
    private Integer qty;

    @NotBlank(message = "Payment wajib diisi!!!")
    private String payment;

}
