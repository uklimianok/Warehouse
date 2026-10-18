package local.warehouse.app.service.employee.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.employee.department.DepartmentRequest;
import local.warehouse.app.entity.employee.Department;
import local.warehouse.app.mapper.employee.department.DepartmentRequestMapper;
import local.warehouse.app.repository.employee.DepartmentRepository;
import local.warehouse.app.repository.employee.PositionRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.employee.DepartmentService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service 
@CacheConfig(cacheNames = "departments")
@RequiredArgsConstructor 
public class DepartmentServiceImpl extends AbstractService<Department, Long> implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    private final DepartmentRequestMapper departmentRequestMapper;

    @Override
    @Cacheable(key = "#id")
    public Department read(Long id) {
        return super.read(id);
    }

    @Override
    public Department create(DepartmentRequest departmentRequest) {
        if (departmentRepository.existsByName(departmentRequest.getName()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));
        
        Department department = new Department();
        department.setCodeName(departmentRequest.getName().replace(' ', '_').toUpperCase());
        department.setRemovable(true);

        return modifyAndSave(department, departmentRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public Department update(long id, DepartmentRequest departmentRequest) {
        Department department = read(id);
        boolean departmentIsChanged = !department.getName().equals(departmentRequest.getName());
        boolean departmentExists = departmentRepository.existsByName(departmentRequest.getName());
        if (departmentIsChanged && departmentExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(department, departmentRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        Department department = read(id);
        if (!department.isRemovable())
            throw new BusinessRuleException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));

        super.delete(id);
    }

    @Override
    protected JpaRepository<Department, Long> getRepository() {
        return departmentRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.DEPARTMENT;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInPosition = positionRepository.existsByDepartmentId(id);
        return activeInPosition;
    }

    private Department modifyAndSave(Department target, DepartmentRequest from) {
        departmentRequestMapper.convertFromRequest(from, target);
        return departmentRepository.save(target);
    }
}
