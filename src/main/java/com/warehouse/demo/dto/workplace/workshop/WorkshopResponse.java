package com.warehouse.demo.dto.workplace.workshop;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WorkshopResponse {
    private long id;
    private String name;
    private BigDecimal standard;
}
