package local.warehouse.app.service.employee.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.employee.organizationType.OrganizationTypeRequest;
import local.warehouse.app.entity.employee.OrganizationType;
import local.warehouse.app.mapper.employee.organizationType.OrganizationTypeRequestMapper;
import local.warehouse.app.repository.employee.OrganizationRepository;
import local.warehouse.app.repository.employee.OrganizationTypeRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.employee.OrganizationTypeService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = "organizationTypes")
@RequiredArgsConstructor
public class OrganizationTypeServiceImpl extends AbstractService<OrganizationType, Long> implements OrganizationTypeService {
    private final OrganizationTypeRepository organizationTypeRepository;
    private final OrganizationRepository organizationRepository;

    private final OrganizationTypeRequestMapper organizationTypeRequestMapper;

    @Override 
    @Cacheable(key = "#id")
    public OrganizationType read(Long id) {
        return super.read(id);
    }

    @Override
    public OrganizationType create(OrganizationTypeRequest organizationTypeRequest) {
        if (organizationTypeRepository.existsByName(organizationTypeRequest.getName()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        OrganizationType organizationType = new OrganizationType();

        return modifyAndSave(organizationType, organizationTypeRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public OrganizationType update(long id, OrganizationTypeRequest organizationTypeRequest) {
        OrganizationType organizationType = read(id);
        boolean nameChanged = !organizationType.getName().equals(organizationTypeRequest.getName());
        boolean nameExists = organizationTypeRepository.existsByName(organizationTypeRequest.getName());
        if (nameChanged && nameExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(organizationType, organizationTypeRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private OrganizationType modifyAndSave(OrganizationType target, OrganizationTypeRequest from) {
        organizationTypeRequestMapper.convertFromRequest(from, target);
        return organizationTypeRepository.save(target);
    }

    @Override
    protected JpaRepository<OrganizationType, Long> getRepository() {
        return organizationTypeRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.ORGANIZATION_TYPE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInOrganization = organizationRepository.existsByOrganizationTypeId(id);
        return activeInOrganization;
    }
}
