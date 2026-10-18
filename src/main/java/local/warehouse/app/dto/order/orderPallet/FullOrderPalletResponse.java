package local.warehouse.app.dto.order.orderPallet;

import local.warehouse.app.dto.item.pallet.PalletResponse;
import local.warehouse.app.dto.order.OrderResponse;
import local.warehouse.app.dto.service.status.StatusResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter 
public class FullOrderPalletResponse extends OrderPalletResponse {
    private StatusResponse status;

    public FullOrderPalletResponse(
        long id, 
        OrderResponse order, 
        PalletResponse pallet, 
        StatusResponse status
    ) {
        super(
            id, 
            order, 
            pallet
        );
        this.status = status;
    }
}
