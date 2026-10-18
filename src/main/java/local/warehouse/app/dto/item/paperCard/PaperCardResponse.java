package local.warehouse.app.dto.item.paperCard;

import local.warehouse.app.dto.order.orderPallet.OrderPalletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class PaperCardResponse {
    private long id;
    private String code;
    private OrderPalletResponse orderPallet;
}
