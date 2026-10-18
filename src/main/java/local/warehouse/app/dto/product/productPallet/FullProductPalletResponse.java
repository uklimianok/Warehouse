package local.warehouse.app.dto.product.productPallet;

import local.warehouse.app.dto.item.pallet.PalletResponse;
import local.warehouse.app.dto.product.productPackage.ProductPackageResponse;
import local.warehouse.app.dto.service.status.StatusResponse;
import local.warehouse.app.dto.workplace.workStation.WorkStationResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullProductPalletResponse extends TransferProductPalletResponse {
    private int packageAmount;
    private StatusResponse status;

    public FullProductPalletResponse(
        long id, 
        ProductPackageResponse productPackage, 
        int packageAmount, 
        PalletResponse pallet, 
        String palletNumber, 
        String groupNumber, 
        StatusResponse status, 
        WorkStationResponse workStation, 
        WorkStationResponse nextWorkStation
    ) {
        super(
            id, 
            productPackage, 
            pallet, 
            palletNumber, 
            groupNumber, 
            workStation, 
            nextWorkStation
        );
        this.packageAmount = packageAmount;
        this.status = status;
    }
}
