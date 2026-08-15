package com.warehouse.demo.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {
    private long storeId;
    private Long gateId;
    private long shiftId;
    private long statusId;
    private String note;
}
