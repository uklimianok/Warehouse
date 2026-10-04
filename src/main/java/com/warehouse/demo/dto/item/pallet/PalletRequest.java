package com.warehouse.demo.dto.item.pallet;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PalletRequest {
    @NotBlank 
    @Size(min = 1, max = 50)
    private String name;

    @NotBlank 
    @Size(min = 1, max = 50)
    private String color;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal length;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal width;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal height;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal weight;
}
