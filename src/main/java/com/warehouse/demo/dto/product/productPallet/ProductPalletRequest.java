package com.warehouse.demo.dto.product.productPallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPalletRequest {
    private long productPackageId;
    private int packageAmount;
    private Long palletId;          // nullable
    private String palletNumber;
    private String groupNumber;
    private long statusId;
    private Long workStationId;     // nullable
    private Long nextWorkStationId; // nullable
}
