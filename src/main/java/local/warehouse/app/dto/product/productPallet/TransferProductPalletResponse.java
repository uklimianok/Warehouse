package local.warehouse.app.dto.product.productPallet;

import local.warehouse.app.dto.item.pallet.PalletResponse;
import local.warehouse.app.dto.product.productPackage.ProductPackageResponse;
import local.warehouse.app.dto.workplace.workStation.WorkStationResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class TransferProductPalletResponse extends ProductPalletResponse {
    private PalletResponse pallet;
    private WorkStationResponse nextWorkStation;

    public TransferProductPalletResponse(
        long id, 
        ProductPackageResponse productPackage, 
        PalletResponse pallet, 
        String palletNumber, 
        String groupNumber, 
        WorkStationResponse workStation, 
        WorkStationResponse nextWorkStation
    ) {
        super(
            id, 
            productPackage, 
            palletNumber, 
            groupNumber, 
            workStation
        );
        this.pallet = pallet;
        this.nextWorkStation = nextWorkStation;
    }
}
