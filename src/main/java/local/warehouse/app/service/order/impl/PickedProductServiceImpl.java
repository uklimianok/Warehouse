package local.warehouse.app.service.order.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.dto.order.pickedProduct.PickedProductRequest;
import local.warehouse.app.entity.employee.Employee;
import local.warehouse.app.entity.order.PickedProduct;
import local.warehouse.app.mapper.order.pickedProduct.PickedProductRequestMapper;
import local.warehouse.app.repository.employee.EmployeeRepository;
import local.warehouse.app.repository.order.PickedProductRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.order.PickedProductService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.action.PositionInheritanceTree;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
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

        PickedProduct savedPickedProduct = pickedProductRepository.save(pickedProduct);



        return savedPickedProduct;
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
