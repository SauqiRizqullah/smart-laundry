package com.gruptiga.smartlaundry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailRequest {
    @NotBlank(message = "Jenis pelayanan laundry wajib dipilih!!!")
    private String serviceTypeId;

    @NotBlank(message = "Jumlah kilo atau satuan wajib diisi!!!")
    private Integer qty;
}
