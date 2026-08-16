package com.warehouse.demo.dto.item.paperCard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaperCardRequest {
    private String code;
    private long orderPalletId;
}
