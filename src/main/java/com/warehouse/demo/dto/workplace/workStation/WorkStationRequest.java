package com.warehouse.demo.dto.workplace.workStation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkStationRequest {
    private String stationNumber;
    private String controlNumber;
    private String type;
    private long workshopId;
}
