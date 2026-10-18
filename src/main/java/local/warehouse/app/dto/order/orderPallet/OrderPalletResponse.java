package local.warehouse.app.dto.order.orderPallet;

import local.warehouse.app.dto.item.pallet.PalletResponse;
import local.warehouse.app.dto.order.OrderResponse;
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
