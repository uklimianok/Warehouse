package local.warehouse.app.service.employee.impl;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import local.warehouse.app.configuration.security.keycloak.service.KeycloakRoleService;
import local.warehouse.app.dto.employee.position.PositionRequest;
import local.warehouse.app.entity.employee.Position;
import local.warehouse.app.mapper.employee.position.PositionRequestMapper;
import local.warehouse.app.repository.employee.EmployeeRepository;
import local.warehouse.app.repository.employee.PositionRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.employee.PositionService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl extends AbstractService<Position, Long> implements PositionService {
    private final PositionRepository positionRepository;
    private final EmployeeRepository employeeRepository;

    private final PositionRequestMapper positionRequestMapper;

    private final KeycloakRoleService keycloakRoleService;

    private static final String LOOP_INHERITANCE_MESSAGE = "This position is already in the chain of position inheritance.";

    @Override
    public Position read(Long id) {
        return super.read(id);
    }

    @Override
    @Transactional
    public Position create(PositionRequest positionRequest) {
        if (positionRepository.existsByName(positionRequest.getName())) 
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Position position = new Position();
        position.setCodeName(positionRequest.getName().replace(' ', '_').toUpperCase());
        position.setRemovable(true);

        positionRequestMapper.convertFromRequest(positionRequest, position);

        Position savedPosition = positionRepository.saveAndFlush(position);

        keycloakRoleService.createRole(savedPosition.getCodeName());

        return savedPosition;
    }

    @Override
    @Transactional
    public Position update(long id, PositionRequest positionRequest) {
        Position position = read(id);
        boolean nameChanged = !position.getName().equals(positionRequest.getName());
        boolean nameExists = positionRepository.existsByName(positionRequest.getName());
        if (nameChanged && nameExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        throwIfInheritanceLooped(position.getId(), positionRequest.getInheritedPositionsId());

        return modifyAndSave(position, positionRequest);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Position position = read(id);
        if (!position.isRemovable())
            throw new BusinessRuleException(MessageHandler.getOutputMessage(OutputMessage.OPERATION_DENIED));

        super.delete(id);
        positionRepository.flush();

        keycloakRoleService.deleteRole(position.getCodeName());
    }

    private Position modifyAndSave(Position target, PositionRequest from) {
        positionRequestMapper.convertFromRequest(from, target);
        return positionRepository.save(target);
    }

    @Override
    protected JpaRepository<Position, Long> getRepository() {
        return positionRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.POSITION;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInEmployee = employeeRepository.existsByPositionId(id);
        boolean isInherited = positionRepository.existsByInheritedPositionsId(id);
        return activeInEmployee || isInherited;
    }

    private void throwIfInheritanceLooped(long targetId, List<Long> parents) {
        if (parents == null) return;

        for (int i = 0; i < parents.size(); i++) {
            if (parents.get(i) == targetId)
                throw new BusinessRuleException(MessageHandler.getOutputMessage(LOOP_INHERITANCE_MESSAGE));
        
            List<Long> grandParents = positionRepository.findInheritedPositionsIdById(parents.get(i));
            throwIfInheritanceLooped(targetId, grandParents);
        }
    }
}
