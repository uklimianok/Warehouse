package local.warehouse.app.flow.setupData;

public record SetupData(
    long productId, 
    long packageId,
    long palletId,
    long workshopId,
    long stationId,
    long gateId
) {}
