package com.gruptiga.smartlaundry.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class AccountResponse {
    private String accountId;

    private String name;

    private String address;

    private String contact;

    private String email;
}
