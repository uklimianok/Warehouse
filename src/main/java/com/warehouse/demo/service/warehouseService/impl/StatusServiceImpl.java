package com.warehouse.demo.service.warehouseService.impl;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.service.status.StatusRequest;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.mapper.service.status.StatusRequestMapper;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.product.ProductPalletRepository;
import com.warehouse.demo.repository.service.StatusRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.warehouseService.StatusService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl extends AbstractService<Status, Long> implements StatusService {
    private final StatusRepository statusRepository;
    private final ProductPalletRepository productPalletRepository;
    private final OrderRepository orderRepository;
    private final OrderPalletRepository orderPalletRepository;

    private final StatusRequestMapper statusRequestMapper;

    private final CacheManager cacheManager;

    @Override 
    @Cacheable(value = "statuses", key = "#id")
    public Status read(Long id) {
        return super.read(id);
    }
    
    @Override
    @Cacheable(value = "statuses", key = "#name + ':' + #type") 
    public Status readByNameAndType(String name, String type) {     // Refactor other services like this if it needs
        return statusRepository.findByNameAndType(name, type)
            .orElseThrow(() ->
                new EntityNotFoundException(Utility.getOutputMessage(getEntityName(), OutputMessage.NOT_FOUND))
            );
    }

    @Override
    public Status create(StatusRequest statusRequest) {
        boolean pairExists = statusRepository.existsByNameAndType(statusRequest.getName(), statusRequest.getType());
        if (pairExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Status status = new Status();
        return modifyAndSave(status, statusRequest);
    }

    @Override
    @CacheEvict(value = "statuses", key = "#id")
    public Status update(long id, StatusRequest statusRequest) {
        Status status = read(id);
        boolean fieldChanged = !status.getName().equals(statusRequest.getName()) || !status.getType().equals(statusRequest.getType());
        boolean pairExists = statusRepository.existsByNameAndType(statusRequest.getName(), statusRequest.getType());
        if (fieldChanged && pairExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        String oldName = status.getName();
        String oldType = status.getType();

        Status savedStatus = modifyAndSave(status, statusRequest);
        cacheManager.getCache("statuses").evict(oldName + ":" + oldType); // Unable to use @CacheEvict with name ant type

        return savedStatus;
    }

    @Override 
    @CacheEvict(value = "statuses", key = "#id")
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
    protected EntityName getEntityName() {
        return EntityName.STATUS;
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
