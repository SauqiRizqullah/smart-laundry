package com.gruptiga.smartlaundry.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerResponse {
    private String customerId;

    private String name;

    private String address;

    private String phoneNumber;

}
