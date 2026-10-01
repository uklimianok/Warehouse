package com.warehouse.demo.dto.workplace.workStation;

import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullWorkStationResponse extends WorkStationResponse {
    private String type;
    private WorkshopResponse workshop;

    public FullWorkStationResponse(long id, String stationNumber, String controlNumber, String type, WorkshopResponse workshop) {
        super(id, stationNumber, controlNumber);
        this.type = type;
        this.workshop = workshop;
    }
}
