package com.gruptiga.smartlaundry.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    @NotBlank(message = "Id pelayanan laundry wajib diisi!!!")
    private String serviceTypeId;

    @NotBlank(message = "Id customer wajib diisi!!!")
    private String customersId;

    @NotNull(message = "Kuantitas dari pelayanan laundry wajib diisi!!!")
    @Min(value = 1, message = "Masukkan jumlah kuantitas yang benar!!!")
    private Integer qty;

    @NotBlank(message = "Metode pembayaran wajib diisi!!!")
    private String payment;

}
