package local.warehouse.app.service.employee.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.employee.organization.OrganizationRequest;
import local.warehouse.app.entity.employee.Organization;
import local.warehouse.app.mapper.employee.organization.OrganizationRequestMapper;
import local.warehouse.app.repository.employee.EmployeeRepository;
import local.warehouse.app.repository.employee.OrganizationRepository;
import local.warehouse.app.repository.order.OrderRepository;
import local.warehouse.app.repository.product.ProductRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.employee.OrganizationService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
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
    public Organization read(Long id) {
        return super.read(id);
    }

    @Override
    public Organization create(OrganizationRequest organizationRequest) {
        if (organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Organization(), organizationRequest);
    }

    @Override
    public Organization update(long id, OrganizationRequest organizationRequest) {
        Organization organization = read(id);
        boolean organizationNumberChanged = !organization.getOrganizationNumber().equals(organizationRequest.getOrganizationNumber());
        boolean organizationNumberExists = organizationRepository.existsByOrganizationNumber(organizationRequest.getOrganizationNumber());
        if (organizationNumberChanged && organizationNumberExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(Entity.ORGANIZATION, OutputMessage.EXISTS));

        return modifyAndSave(organization, organizationRequest);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    private Organization modifyAndSave(Organization target, OrganizationRequest from) {
        organizationRequestMapper.convertFromRequest(from, target);
        return organizationRepository.save(target);
    }

    @Override
    protected JpaRepository<Organization, Long> getRepository() {
        return organizationRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ORGANIZATION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInOrganization = employeeRepository.existsByEmployerOrganizationId(id);
        boolean activeInProduct = productRepository.existsByProducerId(id);
        boolean activeInOrder = orderRepository.existsByStoreId(id);
        return activeInOrganization || activeInProduct || activeInOrder;
    }
}
