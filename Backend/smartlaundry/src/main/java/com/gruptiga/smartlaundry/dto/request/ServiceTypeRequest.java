package com.gruptiga.smartlaundry.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTypeRequest {

    @NotBlank(message = "Id akun laundry wajib diisi!!!")
    private String accountId;

    @NotBlank(message = "Tipe laundry wajib diisi!!!")
    @Enumerated(EnumType.STRING)
    private String type;

    @NotBlank(message = "Jenis pelayanan wajib diisii!!!")
    private String service;

    @NotNull(message = "Harga pelayanan wajib diisi!!!")
    @Min(value = 1, message = "Masukkan harga pelayanan laundry dengan benar!!!")
    private Long price;

    @NotBlank(message = "Keterangan dari pelayanan wajib diisi!!!")
    @Enumerated(EnumType.STRING)
    private String detail;
}
