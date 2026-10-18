package local.warehouse.app.dto.order.pickedProduct;

import java.math.BigDecimal;

import local.warehouse.app.dto.order.orderPallet.OrderPalletResponse;
import local.warehouse.app.dto.product.productPackage.ProductPackageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class PickedProductResponse {
    private long id;
    private OrderPalletResponse orderPallet;
    private ProductPackageResponse productPackage;
    private BigDecimal pickedVolume;
    private boolean isCompleted;
}
