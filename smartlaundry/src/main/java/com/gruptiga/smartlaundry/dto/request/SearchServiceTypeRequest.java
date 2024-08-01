package com.gruptiga.smartlaundry.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchServiceTypeRequest {

    private String type;

    private String service;

}
