package com.warehouse.demo.service.employee.impl;

import com.warehouse.demo.repository.employee.OrganizationRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.OrganizationTypeService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationTypeServiceImpl extends AbstractService<OrganizationType, Long> implements OrganizationTypeService {
    private final OrganizationTypeRepository organizationTypeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    protected JpaRepository<OrganizationType, Long> getRepository() {
        return organizationTypeRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ORGANIZATION_TYPE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInOrganization = organizationRepository.existsByOrganizationTypeId(id);
        return activeInOrganization;
    }

    @Override
    public OrganizationType create(OrganizationTypeRequest organizationTypeRequest) {
        if (organizationTypeRepository.existsByName(organizationTypeRequest.getName()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        OrganizationType organizationType = new OrganizationType();

        return modifyAndSave(organizationType, organizationTypeRequest);
    }

    @Override
    public OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = read(id);
        boolean nameChanged = !organizationType.getName().equals(organizationTypeRequest.getName());
        boolean nameExists = organizationTypeRepository.existsByName(organizationTypeRequest.getName());
        if (nameChanged && nameExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(organizationType, organizationTypeRequest);
    }

    private OrganizationType modifyAndSave(OrganizationType target, OrganizationTypeRequest from) {
        target.setName(from.getName());

        return organizationTypeRepository.save(target);
    }
}
