package com.warehouse.demo.dto.order.pickedProduct;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PickedProductRequest {
    private long orderPalletId;
    private long productPackageId;
    private BigDecimal pickedVolume;
}
