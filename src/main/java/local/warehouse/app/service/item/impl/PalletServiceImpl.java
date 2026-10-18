package local.warehouse.app.service.item.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.item.pallet.PalletRequest;
import local.warehouse.app.entity.item.Pallet;
import local.warehouse.app.mapper.item.pallet.PalletRequestMapper;
import local.warehouse.app.repository.item.PalletRepository;
import local.warehouse.app.repository.order.OrderPalletRepository;
import local.warehouse.app.repository.product.ProductPalletRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.item.PalletService;
import local.warehouse.app.util.info.Entity;
import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = "pallets")
@RequiredArgsConstructor
public class PalletServiceImpl extends AbstractService<Pallet, Long> implements PalletService {
    private final PalletRepository palletRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderPalletRepository orderPalletRepository;

    private final PalletRequestMapper palletRequestMapper;

    @Override 
    @Cacheable(key = "#id")
    public Pallet read(Long id) {
        return super.read(id);
    }

    @Override
    public Pallet create(PalletRequest palletRequest) {
        return modifyAndSave(new Pallet(), palletRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public Pallet update(long id, PalletRequest palletRequest) {
        return modifyAndSave(read(id), palletRequest);
    }

    @Override 
    @CacheEvict(key = "#id")
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
