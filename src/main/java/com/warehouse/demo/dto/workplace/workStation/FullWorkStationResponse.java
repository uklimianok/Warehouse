package com.warehouse.demo.dto.workplace.workStation;

import com.warehouse.demo.dto.workplace.workshop.WorkshopResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FullWorkStationResponse extends OperatorWorkStationResponse {
    private WorkshopResponse workshop;

    public FullWorkStationResponse(long id, String stationNumber, String controlNumber, String type, WorkshopResponse workshop) {
        super(id, stationNumber, controlNumber, type);
        this.workshop = workshop;
    }
}
