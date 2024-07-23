package com.gruptiga.smartlaundry.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceTypeResponse {
    private String serviceTypeId;

    private String type;

    private String service;

    private Long price;

    private String detail;
}
