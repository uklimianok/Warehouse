package com.warehouse.demo.dto.workplace.track;

import com.warehouse.demo.dto.workplace.gate.GateResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TrackResponse {
    private long id;
    private String symbol;
    private GateResponse gate;
}
