package com.gruptiga.smartlaundry.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusUpdateRequest {
    @NotBlank(message = "Status wajib diisi!!!")
    private String status;

}
