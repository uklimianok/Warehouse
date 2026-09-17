package com.warehouse.demo.mapper.employee.organization;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationResolver {
    private final OrganizationRepository organizationRepository;

    public Organization mapOrganization(long organizationId) {
        return organizationRepository.findById(organizationId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.ORGANIZATION, OutputMessage.NOT_FOUND)));
    }
}
