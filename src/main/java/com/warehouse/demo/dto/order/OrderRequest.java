package com.warehouse.demo.dto.order;

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
public class OrderRequest {
    @NotNull 
    @Positive 
    private Long storeId;

    @Positive 
    private Long gateId;

    @NotNull 
    @Positive 
    private Long shiftId;

    @NotNull 
    @Positive 
    private Long statusId;

    private String note;
}
