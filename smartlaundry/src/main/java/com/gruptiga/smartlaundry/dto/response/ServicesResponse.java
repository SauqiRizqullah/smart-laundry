package com.gruptiga.smartlaundry.dto.response;
//

import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.entity.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ServicesResponse {
    private String serviceId;

    private String name;

    private Account account;

    private List<ServiceType> serviceTypes;
}
