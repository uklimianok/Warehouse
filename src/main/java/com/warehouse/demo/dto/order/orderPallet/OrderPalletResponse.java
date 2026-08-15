package com.warehouse.demo.dto.order.orderPallet;

import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.order.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderPalletResponse {
    private long id;
    private OrderResponse order;
    private PalletResponse pallet;
}
