package com.gruptiga.smartlaundry.dto.response;

import com.gruptiga.smartlaundry.entity.Service;
import com.gruptiga.smartlaundry.entity.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServiceTypeResponse {
    private String serviceTypeId;

    private Type type;

    private Service service;

    private Long price;

    private String detail;
}
