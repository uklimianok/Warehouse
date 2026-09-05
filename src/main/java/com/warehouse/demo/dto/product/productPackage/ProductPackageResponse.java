package com.warehouse.demo.dto.product.productPackage;

import java.math.BigDecimal;

import com.warehouse.demo.dto.product.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class ProductPackageResponse {
    private long id;
    private ProductResponse product;
    private int productsAmount;
    private BigDecimal volume;
    private BigDecimal weight;
}
