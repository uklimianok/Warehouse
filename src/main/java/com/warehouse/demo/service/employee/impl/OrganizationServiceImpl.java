package com.warehouse.demo.service.employee.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.employee.organization.OrganizationRequest;
import com.warehouse.demo.entity.employee.Organization;
import com.warehouse.demo.mapper.employee.organization.OrganizationRequestMapper;
import com.warehouse.demo.repository.employee.EmployeeRepository;
import com.warehouse.demo.repository.employee.OrganizationRepository;
import com.warehouse.demo.repository.order.OrderRepository;
import com.warehouse.demo.repository.product.ProductRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.employee.OrganizationService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends AbstractService<Organization, Long> implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    private final OrganizationRequestMapper organizationRequestMapper;

    @Override
    protected JpaRepository<Organization, Long> getRepository() {
        return organizationRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.ORGANIZATION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInOrganization = employeeRepository.existsByEmployerOrganizationId(id);
        boolean activeInProduct = productRepository.existsByProducerId(id);
        boolean activeInOrder = orderRepository.existsByStoreId(id);
        return activeInOrganization || activeInProduct || activeInOrder;
    }

    @Override
    public Organization create(OrganizationRequest organizationRequest) {
        if (organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Organization(), organizationRequest);
    }

    @Override
    public Organization update(long id, OrganizationRequest organizationRequest) {
        Organization organization = read(id);
        boolean organizationNumberChanged = !organization.getOrganizationNumber().equals(organizationRequest.getOrganizationNumber());
        boolean organizationNumberExists = organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber());
        if (organizationNumberChanged && organizationNumberExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(EntityName.ORGANIZATION, OutputMessage.EXISTS));

        return modifyAndSave(organization, organizationRequest);
    }

    private Organization modifyAndSave(Organization target, OrganizationRequest from) {
        organizationRequestMapper.convertFromRequest(from, target);
        return organizationRepository.save(target);
    }
}
