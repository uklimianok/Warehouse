package com.warehouse.demo.dto.order.orderPallet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderPalletRequest {
    private long orderId;
    private long palletId;
    private long statusId;
}
