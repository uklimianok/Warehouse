package com.warehouse.demo.dto.product.productPackage;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPackageRequest {
    private long productId;
    private int productsAmount;
    private BigDecimal volume;
    private BigDecimal weight;
}
