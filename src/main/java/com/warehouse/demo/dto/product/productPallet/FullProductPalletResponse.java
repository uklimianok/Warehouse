package com.warehouse.demo.dto.product.productPallet;

import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;
import com.warehouse.demo.dto.workplace.workStation.WorkStationResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullProductPalletResponse extends TransferProductPalletResponse {
    private int packageAmount;
    private PalletResponse pallet;
    private StatusResponse status;

    public FullProductPalletResponse(long id, ProductPackageResponse productPackage, int packageAmount, PalletResponse pallet, String palletNumber, String groupNumber, StatusResponse status, WorkStationResponse workStation, WorkStationResponse nextWorkStation) {
        super(id, productPackage, pallet, palletNumber, groupNumber, workStation, nextWorkStation);
        this.packageAmount = packageAmount;
        this.status = status;
    }
}
