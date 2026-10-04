package com.warehouse.demo.dto.order.returnProduct;

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
public class ReturnProductRequest {
    @NotNull 
    @Positive 
    private Long orderId;
    
    @NotNull 
    @Positive 
    private Long productId;
    
    @NotNull 
    @Positive 
    private Integer productsAmount;
}
