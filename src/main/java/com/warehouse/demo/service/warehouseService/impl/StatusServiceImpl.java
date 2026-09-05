package com.warehouse.demo.service.warehouseService.impl;

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
    
    @Override 
    public Status readByNameAndType(String name, String type) {
        boolean pairExists = statusRepository.existsByNameAndType(name, type);
        if (!pairExists)
            throw new EntityNotFoundException(Utility.getOutputMessage(getEntityName(), OutputMessage.NOT_FOUND));

        return statusRepository.findByNameAndType(name, type).get();
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
    public Status update(long id, StatusRequest statusRequest) {
        Status status = read(id);
        boolean fieldChanged = !status.getName().equals(statusRequest.getName()) || !status.getType().equals(statusRequest.getType());
        boolean pairExists = statusRepository.existsByNameAndType(statusRequest.getName(), statusRequest.getType());
        if (fieldChanged && pairExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(status, statusRequest);
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
