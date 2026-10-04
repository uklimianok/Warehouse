package com.warehouse.demo.dto.product.productPackage;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPackageRequest {
    @NotNull 
    @Positive 
    private Long productId;
    
    @NotNull 
    @Positive 
    private Integer productsAmount;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal volume;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal weight;
}
