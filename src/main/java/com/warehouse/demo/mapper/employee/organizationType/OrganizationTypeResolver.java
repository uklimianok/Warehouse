package com.warehouse.demo.mapper.employee.organizationType;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.OrganizationType;
import com.warehouse.demo.repository.employee.OrganizationTypeRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationTypeResolver {
    private final OrganizationTypeRepository organizationTypeRepository;

    public OrganizationType mapOrganizationType(long organizationTypeId) {
        return organizationTypeRepository.findById(organizationTypeId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.ORGANIZATION_TYPE, OutputMessage.NOT_FOUND)));
    }
}
