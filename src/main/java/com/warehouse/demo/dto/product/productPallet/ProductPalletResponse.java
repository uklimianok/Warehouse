package com.warehouse.demo.dto.product.productPallet;

import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;
import com.warehouse.demo.dto.workplace.workStation.WorkStationResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class ProductPalletResponse {
    private long id;
    private ProductPackageResponse productPackage;
    private String palletNumber;
    private String groupNumber;
    private WorkStationResponse workStation;
}
