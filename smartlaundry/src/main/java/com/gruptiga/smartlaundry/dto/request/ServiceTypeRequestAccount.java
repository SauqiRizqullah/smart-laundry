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
public class ServiceTypeRequestAccount {
    @NotBlank(message = "Id akun laundry wajib diisi!!!")
    private String serviceTypeId;

    @NotBlank(message = "Id akun laundry wajib diisi!!!")
    private String email;

    @NotBlank(message = "Tipe laundry wajib diisi!!!")
    private String typeId;


    @NotBlank(message = "Service laundry wajib diisi!!!")
    private String serviceId;

    @NotBlank(message = "Harga pelayanan wajib diisi!!!")
    private Long price;

    @NotBlank(message = "Keterangan dari pelayanan wajib diisi!!!")
    @Enumerated(EnumType.STRING)
    private String detail;

    @NotBlank(message = "Image Path laundry wajib diisi!!!")
    private String imagePath;
}

