package com.warehouse.demo.service.employee.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.service.employee.OrganizationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final OrganizationTypeRepository organizationTypeRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public List<Organization> readAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Organization read(long id) {
        throwIfNotExists(id);

        return organizationRepository.findById(id).get();
    }

    @Override
    public Organization create(OrganizationRequest organizationRequest) {
        if (organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber()))
            throw new DataIntegrityViolationException("Organization already exists.");

        Organization organization = modify(new Organization(), organizationRequest);

        return organizationRepository.save(organization);
    }

    @Override
    public Organization update(long id, OrganizationRequest organizationRequest) {
        Organization oldOrganization = read(id);
        boolean organizationNumberChanged = !oldOrganization.getOrganizationNumber().equals(organizationRequest.getOrganizationNumber());
        boolean organizationNumberExists = organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber());
        if (organizationNumberChanged && organizationNumberExists)
            throw new DataIntegrityViolationException("Organization already exists.");

        Organization organization = modify(oldOrganization, organizationRequest);
        
        return organizationRepository.save(organization);
    }

    @Override
    public void delete(long id) {
        throwIfNotExists(id);

        if (employeeRepository.existsByOrganizationId(id))
            throw new DataIntegrityViolationException("Organization is active.");

        organizationRepository.deleteById(id);
    }

    private void throwIfNotExists(long id) {
        if (!organizationRepository.existsById(id))
            throw new EntityNotFoundException("Organization not found.");
    }

    private Organization modify(Organization target, OrganizationRequest from) {
        target.setName(from.getName());
        target.setOrganizationNumber(from.getOrganizationNumber());
        target.setAddress(from.getAddress());
        target.setPhone(from.getPhone());
        target.setEmail(from.getEmail());
        target.setUrl(from.getUrl());

        Optional<OrganizationType> organizationType = organizationTypeRepository.findById(from.getOrganizationTypeId());
        if (organizationType.isPresent()) target.setOrganizationType(organizationType.get());
        else throw new EntityNotFoundException("Organization type not found.");

        return target;
    }
}
