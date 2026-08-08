package com.warehouse.demo.service.employee.impl;

import com.warehouse.demo.repository.employee.OrganizationRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.service.AbstractCrudService;
import com.warehouse.demo.service.employee.OrganizationTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationTypeServiceImpl extends AbstractCrudService<OrganizationType, Long> implements OrganizationTypeService {
    private final OrganizationTypeRepository organizationTypeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    protected JpaRepository<OrganizationType, Long> getRepository() {
        return organizationTypeRepository;
    }

    @Override
    protected String getEntityName() {
        return "Organization type";
    }

    @Override
    protected boolean isUsed(Long id) {
        return organizationRepository.existsByOrganizationTypeId(id);
    }

    @Override
    public OrganizationType create(OrganizationTypeRequest organizationTypeRequest) {
        if (organizationTypeRepository.existsByName(organizationTypeRequest.getName()))
            throw new DataIntegrityViolationException("Organization type already exists.");

        OrganizationType organizationType = new OrganizationType();
        organizationType.setName(organizationTypeRequest.getName());

        return organizationTypeRepository.save(organizationType);
    }

    @Override
    public OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = read(id);
        organizationType.setName(organizationTypeRequest.getName());

        return organizationTypeRepository.save(organizationType);
    }
}
