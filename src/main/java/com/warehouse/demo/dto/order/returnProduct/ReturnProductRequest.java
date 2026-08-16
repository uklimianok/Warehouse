package com.warehouse.demo.dto.order.returnProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReturnProductRequest {
    private long orderId;
    private long productId;
    private int productsAmount;
}
