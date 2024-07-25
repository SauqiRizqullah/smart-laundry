package com.gruptiga.smartlaundry.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {

    @NotBlank(message = "Nama harus diisi!!!")
    private String name;

    @NotBlank(message = "Alamat harus diisi!!!")
    private String address;

    @NotBlank(message = "Nomor HP harus diisi!!!")
    @Pattern(regexp = "^08\\d{9,11}$", message = "Mobile phone number's pattern must be started by 08 and has 9 until 12 digits")
    private String phoneNumber;

    @NotBlank(message = "Id akun laundry harus diisi!!!")
    private String accountId;
}
