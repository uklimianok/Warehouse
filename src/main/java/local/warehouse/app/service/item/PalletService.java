package local.warehouse.app.service.item;

import local.warehouse.app.dto.item.pallet.PalletRequest;
import local.warehouse.app.entity.item.Pallet;
import local.warehouse.app.service.BaseService;

public interface PalletService extends BaseService<Pallet, Long> {
    Pallet create(PalletRequest palletRequest);
    Pallet update(long id, PalletRequest palletRequest);
}
