package com.warehouse.demo.dto.order.returnProduct;

import com.warehouse.demo.dto.order.OrderResponse;
import com.warehouse.demo.dto.product.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReturnProductResponse {
    private long id;
    private OrderResponse order;
    private ProductResponse product;
    private int productsAmount;
}
