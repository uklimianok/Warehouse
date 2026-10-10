package com.warehouse.demo.dto.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {
    @NotBlank 
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank 
    @Size(min = 1, max = 30)
    private String barcodeNumber;

    @NotNull 
    @PositiveOrZero 
    private BigDecimal cost;

    @NotNull 
    @Positive 
    private Long producerId;
}
