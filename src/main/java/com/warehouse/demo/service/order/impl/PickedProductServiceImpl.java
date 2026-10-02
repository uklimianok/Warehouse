package com.warehouse.demo.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.order.pickedProduct.PickedProductRequest;
import com.warehouse.demo.entity.employee.Employee;
import com.warehouse.demo.entity.order.PickedProduct;
import com.warehouse.demo.mapper.order.pickedProduct.PickedProductRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.order.PickedProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.order.PickedProductService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.action.PositionInheritanceTree;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PickedProductServiceImpl extends AbstractService<PickedProduct, Long> implements PickedProductService {
    private final PickedProductRepository pickedProductRepository;
    private final EmployeeRepository employeeRepository;

    private final PickedProductRequestMapper pickedProductRequestMapper;

    private final PositionInheritanceTree positionInheritanceTree;

    @Override
    public PickedProduct create(PickedProductRequest pickedProductRequest) {
        return modifyAndSave(new PickedProduct(), pickedProductRequest);
    }

    @Override
    public PickedProduct update(long id, PickedProductRequest pickedProductRequest, String employeeNumber) {
        PickedProduct pickedProduct = read(id);

        Employee callerEmployee = employeeRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(Entity.EMPLOYEE, OutputMessage.NOT_FOUND)));
        if (positionInheritanceTree.isOneOrDescendant(callerEmployee.getPosition(), "GOODS_PICKER")) 
            pickedProductRequestMapper.convertFromGoodsPickerRequest(pickedProductRequest, pickedProduct);
        else
            pickedProductRequestMapper.convertFromRequest(pickedProductRequest, pickedProduct);

        return pickedProductRepository.save(pickedProduct);
    }

    private PickedProduct modifyAndSave(PickedProduct target, PickedProductRequest from) {
        pickedProductRequestMapper.convertFromRequest(from, target);
        return pickedProductRepository.save(target);
    }

    @Override
    protected JpaRepository<PickedProduct, Long> getRepository() {
        return pickedProductRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.PICKED_PRODUCT;
    }
}
