package com.warehouse.demo.dto.service.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusResponse {
    private long id;
    private String name;
    private String type;
}
