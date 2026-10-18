package local.warehouse.app.dto.order.returnProduct;

import local.warehouse.app.dto.order.OrderResponse;
import local.warehouse.app.dto.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class ReturnProductResponse {
    private long id;
    private OrderResponse order;
    private ProductResponse product;
    private int productsAmount;
}
