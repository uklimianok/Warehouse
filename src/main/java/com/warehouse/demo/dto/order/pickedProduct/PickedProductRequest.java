package com.warehouse.demo.dto.order.pickedProduct;

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
public class PickedProductRequest {
    @NotNull
    @Positive 
    private Long orderPalletId;
    
    @NotNull 
    @Positive 
    private Long packageId;
    
    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal pickedVolume;

    @NotNull 
    private Boolean completed;
}
