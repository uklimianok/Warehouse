package local.warehouse.app.service.employee.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.employee.shift.ShiftRequest;
import local.warehouse.app.entity.employee.Shift;
import local.warehouse.app.mapper.employee.shift.ShiftRequestMapper;
import local.warehouse.app.repository.employee.EmployeeRepository;
import local.warehouse.app.repository.employee.ShiftRepository;
import local.warehouse.app.repository.order.OrderRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.employee.ShiftService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = "shifts")
@RequiredArgsConstructor
public class ShiftServiceImpl extends AbstractService<Shift, Long> implements ShiftService {
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;

    private final ShiftRequestMapper shiftRequestMapper;

    @Override
    @Cacheable(key = "#id")
    public Shift read(Long id) {
        return super.read(id);
    }

    @Override
    public Shift create(ShiftRequest shiftRequest) {
        if (shiftRepository.existsBySymbol(shiftRequest.getSymbol()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Shift(), shiftRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public Shift update(long id, ShiftRequest shiftRequest) {
        Shift shift = read(id);
        boolean shiftChanged = !shift.getSymbol().equals(shiftRequest.getSymbol());
        boolean shiftExists = shiftRepository.existsBySymbol(shiftRequest.getSymbol());
        if (shiftChanged && shiftExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(shift, shiftRequest);
    }

    @Override 
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Shift modifyAndSave(Shift target, ShiftRequest from) {
        shiftRequestMapper.convertFromRequest(from, target);
        return shiftRepository.save(target);
    }

    @Override
    protected JpaRepository<Shift, Long> getRepository() {
        return shiftRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.SHIFT;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInEmployee = employeeRepository.existsByShiftId(id);
        boolean activeInOrder = orderRepository.existsByShiftId(id);
        return activeInEmployee || activeInOrder;
    }
}
