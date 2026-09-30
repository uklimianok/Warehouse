package com.warehouse.demo.service.employee.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.department.DepartmentRequest;
import com.warehouse.demo.entity.employee.Department;
import com.warehouse.demo.mapper.employee.department.DepartmentRequestMapper;
import com.warehouse.demo.repository.employee.DepartmentRepository;
import com.warehouse.demo.repository.employee.PositionRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.DepartmentService;
import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class DepartmentServiceImpl extends AbstractService<Department, Long> implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    private final DepartmentRequestMapper departmentRequestMapper;

    @Override
    @Cacheable(value = "departments", key = "#id")
    public Department create(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setCodeName(departmentRequest.getName().toUpperCase());

        return modifyAndSave(department, departmentRequest);
    }

    @Override
    @CacheEvict(value = "departments", key = "#id")
    public Department update(long id, DepartmentRequest departmentRequest) {
        Department department = read(id);
        boolean departmentIsChanged = !department.getName().equals(departmentRequest.getName());
        boolean departmentExists = departmentRepository.existsByName(departmentRequest.getName());
        if (departmentIsChanged && departmentExists)
            throw new DataIntegrityViolationException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(department, departmentRequest);
    }

    @Override
    @CacheEvict(value = "departments", key = "#id")
    public void delete(Long id) {
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
