package com.warehouse.demo.dto.workplace.workshop;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkshopRequest {
    private String name;
    private BigDecimal standard;
}
