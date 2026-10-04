package com.warehouse.demo.dto.product.productPallet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPalletRequest {
    @NotNull 
    @Positive 
    private Long productPackageId;
    
    @NotNull 
    @Positive 
    private Integer packageAmount;
    
    @NotNull 
    @Positive 
    private Long palletId;
    
    @NotBlank 
    @Size(min = 1, max = 20)
    private String groupNumber;

    @NotNull 
    @Positive 
    private Long statusId;
    
    @Positive 
    private Long workStationId;
    
    @Positive 
    private Long nextWorkStationId;
}
