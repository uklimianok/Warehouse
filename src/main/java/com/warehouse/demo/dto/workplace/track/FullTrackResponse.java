package com.warehouse.demo.dto.workplace.track;

import java.math.BigDecimal;

import com.warehouse.demo.dto.workplace.gate.GateResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullTrackResponse extends TrackResponse {
    private BigDecimal length;
    private BigDecimal width;

    public FullTrackResponse(long id, String symbol, BigDecimal length, BigDecimal width, GateResponse gateResponse) {
        super(id, symbol, gateResponse);
        this.length = length;
        this.width = width;
    }
}
