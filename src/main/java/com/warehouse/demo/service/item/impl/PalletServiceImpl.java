package com.warehouse.demo.service.item.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.item.pallet.PalletRequest;
import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.mapper.item.pallet.PalletRequestMapper;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.item.PalletService;
import com.warehouse.demo.util.info.Entity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PalletServiceImpl extends AbstractService<Pallet, Long> implements PalletService {
    private final PalletRepository palletRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderPalletRepository orderPalletRepository;

    private final PalletRequestMapper palletRequestMapper;

    @Override 
    @Cacheable(value = "pallets", key = "#id")
    public Pallet read(Long id) {
        return super.read(id);
    }

    @Override
    public Pallet create(PalletRequest palletRequest) {
        return modifyAndSave(new Pallet(), palletRequest);
    }

    @Override
    @CacheEvict(value = "pallets", key = "#id")
    public Pallet update(long id, PalletRequest palletRequest) {
        return modifyAndSave(read(id), palletRequest);
    }

    @Override 
    @CacheEvict(value = "pallets", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Pallet modifyAndSave(Pallet target, PalletRequest from) {
        palletRequestMapper.convertFromRequest(from, target);
        return palletRepository.save(target);
    }

    @Override
    protected JpaRepository<Pallet, Long> getRepository() {
        return palletRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.PALLET;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPallet = productPalletRepository.existsByPalletId(id);
        boolean activeInOrderPallet = orderPalletRepository.existsByPalletId(id);
        return activeInProductPallet || activeInOrderPallet;
    }
}
