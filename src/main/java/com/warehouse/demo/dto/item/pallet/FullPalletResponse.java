package com.warehouse.demo.dto.item.pallet;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FullPalletResponse extends PalletResponse {
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal weight;

    public FullPalletResponse(long id, String name, String color, BigDecimal length, BigDecimal width, BigDecimal height, BigDecimal weight) {
        super(id, name, color);
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }
}
