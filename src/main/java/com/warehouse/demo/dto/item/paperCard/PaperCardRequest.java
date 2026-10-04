package com.warehouse.demo.dto.item.paperCard;

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
public class PaperCardRequest {
    @NotBlank 
    @Size(min = 14, max = 14)
    private String code;

    @NotNull 
    @Positive 
    private long orderPalletId;
}
