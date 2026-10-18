package local.warehouse.app.mapper.employee.organizationType;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.employee.OrganizationType;
import local.warehouse.app.repository.employee.OrganizationTypeRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationTypeResolver {
    private final OrganizationTypeRepository organizationTypeRepository;

    public OrganizationType mapOrganizationType(long organizationTypeId) {
        return organizationTypeRepository.findById(organizationTypeId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.ORGANIZATION_TYPE, OutputMessage.NOT_FOUND)));
    }
}
