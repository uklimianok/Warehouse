package com.warehouse.demo.service.employee.impl;

import com.warehouse.demo.repository.employee.OrganizationRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organization.organizationType.OrganizationTypeRequest;
import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.service.employee.OrganizationTypeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationTypeServiceImpl implements OrganizationTypeService {
    private final OrganizationTypeRepository organizationTypeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public List<OrganizationType> readAll() {
        return organizationTypeRepository.findAll();
    }

    @Override
    public OrganizationType readById(long id) {
        Optional<OrganizationType> organizationType = organizationTypeRepository.findById(id);
        if (organizationType.isPresent()) return organizationType.get();
        else throw new EntityNotFoundException("Organization type not found.");
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
        OrganizationType organizationType = this.readById(id);
        organizationType.setName(organizationTypeRequest.getName());

        return organizationTypeRepository.save(organizationType);
    }

    @Override
    public void deleteById(long id) {
        if (!organizationTypeRepository.existsById(id))
            throw new EntityNotFoundException("Organization type not found.");

        if (organizationRepository.existsByOrganizationTypeId(id))
            throw new DataIntegrityViolationException("Organization type is active.");

        organizationTypeRepository.deleteById(id);
    }
}
