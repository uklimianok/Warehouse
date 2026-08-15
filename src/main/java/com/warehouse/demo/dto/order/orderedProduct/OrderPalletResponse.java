package com.warehouse.demo.dto.order.orderedProduct;

import java.math.BigDecimal;

import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.product.productPackage.ProductPackageResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderPalletResponse {
    private long id;
    private OrderResponse order;
    private ProductPackageResponse productPackage;
    private BigDecimal orderedVolume;
}
