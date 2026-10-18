package local.warehouse.app.service.warehouseService.impl;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.dto.service.status.StatusRequest;
import local.warehouse.app.entity.service.Status;
import local.warehouse.app.mapper.service.status.StatusRequestMapper;
import local.warehouse.app.repository.order.OrderPalletRepository;
import local.warehouse.app.repository.order.OrderRepository;
import local.warehouse.app.repository.product.ProductPalletRepository;
import local.warehouse.app.repository.service.StatusRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.warehouseService.StatusService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = "statuses")
@RequiredArgsConstructor
public class StatusServiceImpl extends AbstractService<Status, Long> implements StatusService {
    private final StatusRepository statusRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderRepository orderRepository;
    private final OrderPalletRepository orderPalletRepository;

    private final StatusRequestMapper statusRequestMapper;

    private final CacheManager cacheManager;

    @Override 
    @Cacheable(key = "#id")
    public Status read(Long id) {
        return super.read(id);
    }
    
    @Override
    @Cacheable(key = "#name + ':' + #type") 
    public Status readByNameAndType(String name, String type) {
        return statusRepository.findByNameAndType(name, type)
            .orElseThrow(() ->
                new EntityNotFoundException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.NOT_FOUND))
            );
    }

    @Override
    public Status create(StatusRequest statusRequest) {
        boolean pairExists = statusRepository.existsByNameAndType(statusRequest.getName(), statusRequest.getType());
        if (pairExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Status status = new Status();
        return modifyAndSave(status, statusRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public Status update(long id, StatusRequest statusRequest) {
        Status status = read(id);
        boolean fieldChanged = !status.getName().equals(statusRequest.getName()) || !status.getType().equals(statusRequest.getType());
        boolean pairExists = statusRepository.existsByNameAndType(statusRequest.getName(), statusRequest.getType());
        if (fieldChanged && pairExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        String oldName = status.getName();
        String oldType = status.getType();

        Status savedStatus = modifyAndSave(status, statusRequest);
        cacheManager.getCache("statuses").evict(oldName + ":" + oldType);

        return savedStatus;
    }

    @Override 
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        Status status = read(id);
        super.delete(id);
        cacheManager.getCache("statuses").evict(status.getName() + ":" + status.getType()); // Cache is deleted only after DB operation
    }

    @Override
    protected JpaRepository<Status, Long> getRepository() {
        return statusRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.STATUS;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInProductPallet = productPalletRepository.existsByStatusId(id);
        boolean activeInOrder = orderRepository.existsByStatusId(id);
        boolean activeInOrderPallet = orderPalletRepository.existsByStatusId(id);

        return activeInProductPallet || activeInOrder || activeInOrderPallet;
    }

    private Status modifyAndSave(Status target, StatusRequest from) {
        statusRequestMapper.convertFromRequest(from, target);
        return statusRepository.save(target);
    }
}
