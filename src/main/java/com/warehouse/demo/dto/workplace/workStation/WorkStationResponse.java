package com.warehouse.demo.dto.workplace.workStation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WorkStationResponse {
    private long id;
    private String stationNumber;
    private String controlNumber;
}
