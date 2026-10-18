package local.warehouse.app.dto.product.productPallet;

import local.warehouse.app.dto.product.productPackage.ProductPackageResponse;
import local.warehouse.app.dto.workplace.workStation.WorkStationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class ProductPalletResponse {
    private long id;
    private ProductPackageResponse productPackage;
    private String palletNumber;
    private String groupNumber;
    private WorkStationResponse workStation;
}
