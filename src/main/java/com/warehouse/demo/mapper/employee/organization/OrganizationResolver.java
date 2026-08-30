package com.warehouse.demo.mapper.employee.organization;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationResolver {
    private final OrganizationRepository organizationRepository;

    public Organization mapOrganization(long organizationId) {
        return organizationRepository.findById(organizationId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.ORGANIZATION, OutputMessage.NOT_FOUND)));
    }
}
