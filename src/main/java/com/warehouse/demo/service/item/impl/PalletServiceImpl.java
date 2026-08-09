package com.warehouse.demo.service.item.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.item.pallet.PalletRequest;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.item.PalletService;
import com.warehouse.demo.util.EntityName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PalletServiceImpl extends AbstractService<Pallet, Long> implements PalletService {
    private final PalletRepository palletRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderPalletRepository orderPalletRepository;

    @Override
    public Pallet create(PalletRequest palletRequest) {
        Pallet pallet = new Pallet();
        return modifyAndSave(pallet, palletRequest);
    }

    @Override
    public Pallet update(long id, PalletRequest palletRequest) {
        Pallet pallet = read(id);
        return modifyAndSave(pallet, palletRequest);
    }

    @Override
    protected JpaRepository<Pallet, Long> getRepository() {
        return palletRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PALLET;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPallet = productPalletRepository.existsByPalletId(id);
        boolean activeInOrderPallet = orderPalletRepository.existsByPalletId(id);
        return activeInProductPallet || activeInOrderPallet;
    }

    private Pallet modifyAndSave(Pallet target, PalletRequest from) {
        target.setName(from.getName());
        target.setColor(from.getColor());
        target.setLength(from.getLength());
        target.setWidth(from.getWidth());
        target.setHeight(from.getHeight());
        target.setWeight(from.getWeight());

        return palletRepository.save(target);
    }
}
