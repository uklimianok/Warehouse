package com.warehouse.demo.service.item;

import com.warehouse.demo.dto.item.pallet.PalletRequest;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.service.BaseService;

public interface PalletService extends BaseService<Pallet, Long> {
    Pallet create(PalletRequest palletRequest);
    Pallet update(long id, PalletRequest palletRequest);
}
