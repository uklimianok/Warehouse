package local.warehouse.app.service.workplace.impl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.workplace.gate.GateRequest;
import local.warehouse.app.entity.workplace.Gate;
import local.warehouse.app.mapper.workplace.gate.GateRequestMapper;
import local.warehouse.app.repository.order.OrderRepository;
import local.warehouse.app.repository.workplace.GateRepository;
import local.warehouse.app.repository.workplace.TrackRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.workplace.GateService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@CacheConfig(cacheNames = "gates")
@RequiredArgsConstructor
public class GateServiceImpl extends AbstractService<Gate, Long> implements GateService {
    private final GateRepository gateRepository;
    private final TrackRepository trackRepository;
    private final OrderRepository orderRepository;

    private final GateRequestMapper gateRequestMapper;

    @Override 
    @Cacheable(key = "#id")
    public Gate read(Long id) {
        return super.read(id);
    }
    
    @Override
    public Gate create(GateRequest gateRequest) {
        if (gateRepository.existsBySymbol(gateRequest.getSymbol()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Gate(), gateRequest);
    }

    @Override
    @CacheEvict(key = "#id")
    public Gate update(long id, GateRequest gateRequest) {
        Gate gate = read(id);
        boolean gateChanged = !gate.getSymbol().equals(gateRequest.getSymbol());
        boolean gateExists = gateRepository.existsBySymbol(gateRequest.getSymbol());
        if (gateChanged && gateExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(gate, gateRequest);
    }

    @Override 
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Gate modifyAndSave(Gate target, GateRequest from) {
        gateRequestMapper.convertFromRequest(from, target);
        return gateRepository.save(target);
    }

    @Override
    protected JpaRepository<Gate, Long> getRepository() {
        return gateRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.GATE;
    }

    @Override
    protected boolean isUsed(Long id) {
        boolean activeInTrack = trackRepository.existsByGateId(id);
        boolean activeInOrder = orderRepository.existsByGateId(id);
        return activeInTrack || activeInOrder;
    }
}
