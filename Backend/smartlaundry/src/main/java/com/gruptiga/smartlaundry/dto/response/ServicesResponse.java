package com.gruptiga.smartlaundry.dto.response;
//

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ServicesResponse {
    private String serviceId;

    private String name;
}
