package com.warehouse.demo.dto.item.pallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class PalletResponse {
    private long id;
    private String name;
    private String color;
}
