package com.warehouse.demo.event.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor 
@AllArgsConstructor
@Getter 
@Setter 
public class ProductPalletEvent {
    private long productPackageId;
    private String palletNumber;
    private String groupNumber;
    private Long workStationId;
    private Long nextWorkStationId;
}
