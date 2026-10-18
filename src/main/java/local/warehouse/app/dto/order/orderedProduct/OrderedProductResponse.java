package local.warehouse.app.dto.order.orderedProduct;

import java.math.BigDecimal;

import local.warehouse.app.dto.order.OrderResponse;
import local.warehouse.app.dto.product.productPackage.ProductPackageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class OrderedProductResponse {
    private long id;
    private OrderResponse order;
    private ProductPackageResponse productPackage;
    private BigDecimal orderedVolume;
}
