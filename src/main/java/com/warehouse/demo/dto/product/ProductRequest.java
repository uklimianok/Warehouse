package com.warehouse.demo.dto.product;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
    private String name;
    private String barcodeNumber;
    private BigDecimal cost;
    private long producerId;
}
