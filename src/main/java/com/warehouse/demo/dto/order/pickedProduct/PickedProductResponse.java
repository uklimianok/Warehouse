package com.warehouse.demo.dto.order.pickedProduct;

import java.math.BigDecimal;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PickedProductResponse {
    private long id;
    private OrderPalletResponse orderPallet;
    private ProductPackageResponse productPackage;
    private BigDecimal pickedVolume;
}
