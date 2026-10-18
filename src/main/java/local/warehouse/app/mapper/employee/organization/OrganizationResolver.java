package local.warehouse.app.mapper.employee.organization;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.employee.Organization;
import local.warehouse.app.repository.employee.OrganizationRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrganizationResolver {
    private final OrganizationRepository organizationRepository;

    public Organization mapOrganization(long organizationId) {
        return organizationRepository.findById(organizationId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.ORGANIZATION, OutputMessage.NOT_FOUND)));
    }
}
