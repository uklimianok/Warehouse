package com.warehouse.demo.flow.setupData;

public record SetupData(
    long productId, 
    long packageId,
    long palletId,
    long workshopId,
    long stationId,
    long gateId
) {}
