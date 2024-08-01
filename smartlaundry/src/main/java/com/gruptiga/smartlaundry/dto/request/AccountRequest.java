package com.gruptiga.smartlaundry.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {
    @NotBlank(message = "Id diisi!!!")
    private String id;

    @NotBlank(message = "Nama laundry wajib diisi!!!")
    private String name;

    @NotBlank(message = "Alamat laundry wajib diisi!!!")
    private String address;

    @NotBlank(message = "Kontak laundry wajib diisi!!!")
    @Pattern(regexp = "^08\\d{9,11}$", message = "Mobile phone number's pattern must be started by 08 and has 9 until 12 digits")
    private String contact;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", message = "Format email harus sesuai!!!")
    @NotBlank(message = "Email akun laundry wajib diisi!!!")
    private String email;

    @NotBlank(message = "Password akun laundry wajib diisi!!!")
    private String password;

    private MultipartFile image;
}
