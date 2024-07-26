package com.gruptiga.smartlaundry.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceTypeRequest {

    @NotBlank(message = "Id akun laundry wajib diisi!!!")
    private String email;

    @NotBlank(message = "Tipe laundry wajib diisi!!!")
    @Enumerated(EnumType.STRING)
    private String type;

    @NotBlank(message = "Jenis pelayanan wajib diisii!!!")
    private String service;

    @NotBlank(message = "Harga pelayanan wajib diisi!!!")
    private Long price;

    @NotBlank(message = "Keterangan dari pelayanan wajib diisi!!!")
    @Enumerated(EnumType.STRING)
    private String detail;
}
