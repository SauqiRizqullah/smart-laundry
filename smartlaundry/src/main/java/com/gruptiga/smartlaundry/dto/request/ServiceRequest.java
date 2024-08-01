package com.gruptiga.smartlaundry.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest {
    @NotBlank(message = "Service Id harus diisi!!!")
    private String serviceId;

    @NotBlank(message = "Name harus diisi!!!")
    private String name;

    @NotBlank(message = "email akun laundry harus diisi!!!")
    private String emailAccount;
}
