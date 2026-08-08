package com.warehouse.demo.service.employee.impl;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.service.AbstractCrudService;
import com.warehouse.demo.service.employee.OrganizationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends AbstractCrudService<Organization, Long> implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationTypeRepository organizationTypeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    protected JpaRepository<Organization, Long> getRepository() {
        return organizationRepository;
    }

    @Override
    protected String getEntityName() {
        return "Organization";
    }

    @Override
    protected boolean isUsed(Long id) {
        return employeeRepository.existsByOrganizationId(id);
    }

    @Override
    public Organization create(OrganizationRequest organizationRequest) {
        if (organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber()))
            throw new DataIntegrityViolationException("Organization already exists.");

        return modifyAndSave(new Organization(), organizationRequest);
    }

    @Override
    public Organization update(long id, OrganizationRequest organizationRequest) {
        Organization organization = read(id);
        boolean organizationNumberChanged = !organization.getOrganizationNumber().equals(organizationRequest.getOrganizationNumber());
        boolean organizationNumberExists = organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber());
        if (organizationNumberChanged && organizationNumberExists)
            throw new DataIntegrityViolationException("Organization already exists.");

        return modifyAndSave(organization, organizationRequest);
    }

    private Organization modifyAndSave(Organization target, OrganizationRequest from) {
        target.setName(from.getName());
        target.setOrganizationNumber(from.getOrganizationNumber());
        target.setAddress(from.getAddress());
        target.setPhone(from.getPhone());
        target.setEmail(from.getEmail());
        target.setUrl(from.getUrl());

        Optional<OrganizationType> organizationType = organizationTypeRepository.findById(from.getOrganizationTypeId());
        if (organizationType.isPresent()) target.setOrganizationType(organizationType.get());
        else throw new EntityNotFoundException("Organization type not found.");

        return organizationRepository.save(target);
    }
}
