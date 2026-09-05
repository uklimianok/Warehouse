package com.warehouse.demo.dto.order.orderPallet;

import com.warehouse.demo.dto.item.pallet.PalletResponse;
import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.service.status.StatusResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullOrderPalletResponse extends OrderPalletResponse {
    private StatusResponse status;

    public FullOrderPalletResponse(long id, OrderResponse order, PalletResponse pallet, StatusResponse status) {
        super(id, order, pallet);
        this.status = status;
    }
}
