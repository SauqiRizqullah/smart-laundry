package com.gruptiga.smartlaundry.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TypeRequest {

    @NotBlank(message = "Type id harus diisi!!!")
    private String typeId;

    @NotBlank(message = "Type name harus diisi!!!")
    private String name;

    @NotBlank(message = "email akun laundry harus diisi!!!")
    private String emailAccount;

}
