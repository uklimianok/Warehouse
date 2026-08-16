package com.warehouse.demo.dto.item.paperCard;

import com.warehouse.demo.dto.order.orderPallet.OrderPalletResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaperCardResponse {
    private long id;
    private String code;
    private OrderPalletResponse orderPallet;
}
