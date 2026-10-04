package com.warehouse.demo.dto.workplace.workshop;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkshopRequest {
    @NotBlank 
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank 
    @DecimalMin("1.00")
    private BigDecimal standard;
}
