package com.warehouse.demo.dto.workplace.track;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackRequest {
    private String symbol;
    private BigDecimal length;
    private BigDecimal width;
    private long gateId;
}
