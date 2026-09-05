package com.warehouse.demo.dto.order.orderPallet;

import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.order.OrderResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class OrderPalletResponse {
    private long id;
    private OrderResponse order;
    private PalletResponse pallet;
}
