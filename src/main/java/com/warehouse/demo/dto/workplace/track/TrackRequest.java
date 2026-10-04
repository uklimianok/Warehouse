package com.warehouse.demo.dto.workplace.track;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
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
public class TrackRequest {
    @NotBlank 
    @Size(min = 1, max = 30)
    private String symbol;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal length;

    @NotNull 
    @DecimalMin("0.001")
    private BigDecimal width;

    @Positive 
    private Long gateId;
}
