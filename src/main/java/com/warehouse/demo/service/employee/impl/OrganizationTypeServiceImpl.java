package com.warehouse.demo.service.employee.impl;

import com.warehouse.demo.repository.employee.OrganizationRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.mapper.employee.organizationType.OrganizationTypeRequestMapper;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.OrganizationTypeService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationTypeServiceImpl extends AbstractService<OrganizationType, Long> implements OrganizationTypeService {
    private final OrganizationTypeRepository organizationTypeRepository;
    private final OrganizationRepository organizationRepository;

    private final OrganizationTypeRequestMapper organizationTypeRequestMapper;

    @Override 
    @Cacheable(value = "organizationTypes", key = "#id")
    public OrganizationType read(Long id) {
        return super.read(id);
    }

    @Override
    public OrganizationType create(OrganizationTypeRequest organizationTypeRequest) {
        if (organizationTypeRepository.existsByName(organizationTypeRequest.getName()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        OrganizationType organizationType = new OrganizationType();

        return modifyAndSave(organizationType, organizationTypeRequest);
    }

    @Override
    @CacheEvict(value = "organizationTypes", key = "#id")
    public OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = read(id);
        boolean nameChanged = !organizationType.getName().equals(organizationTypeRequest.getName());
        boolean nameExists = organizationTypeRepository.existsByName(organizationTypeRequest.getName());
        if (nameChanged && nameExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(organizationType, organizationTypeRequest);
    }

    @Override
    @CacheEvict(value = "organizationTypes", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private OrganizationType modifyAndSave(OrganizationType target, OrganizationTypeRequest from) {
        organizationTypeRequestMapper.convertFromRequest(from, target);
        return organizationTypeRepository.save(target);
    }

    @Override
    protected JpaRepository<OrganizationType, Long> getRepository() {
        return organizationTypeRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ORGANIZATION_TYPE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInOrganization = organizationRepository.existsByOrganizationTypeId(id);
        return activeInOrganization;
    }
}
