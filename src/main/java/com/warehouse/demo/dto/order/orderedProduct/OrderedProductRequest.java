package com.warehouse.demo.dto.order.orderedProduct;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderedProductRequest {
    private long orderId;
    private long packageId;
    private BigDecimal orderedVolume;
}
