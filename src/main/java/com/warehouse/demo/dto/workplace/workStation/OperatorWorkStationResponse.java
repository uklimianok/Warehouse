package com.warehouse.demo.dto.workplace.workStation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class OperatorWorkStationResponse extends WorkStationResponse {
    private String type;

    public OperatorWorkStationResponse(long id, String stationNumber, String controlNumber, String type) {
        super(id, stationNumber, controlNumber);
        this.type = type;
    }
}
