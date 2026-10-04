package com.warehouse.demo.dto.order.orderPallet;

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
public class OrderPalletRequest {
    @NotNull 
    @Positive 
    private Long orderId;
    
    @NotNull
    @Positive 
    private Long palletId;
    
    @NotNull 
    @Positive 
    private Long statusId;
}
