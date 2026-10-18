package local.warehouse.app.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;

public abstract class AbstractService<T, ID> {
    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract Entity getEntityName();

    public List<T> readAll() {
        return getRepository().findAll();
    }

    public T read(ID id) {
        return getRepository().findById(id)
            .orElseThrow(() -> new EntityNotFoundException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.NOT_FOUND)));
    }

    public void delete(ID id) {
        throwIfNotExists(id);
        throwIfActive(id);
        getRepository().deleteById(id);
    }

    protected boolean isUsed(ID id) {
        return false;
    }

    protected void throwIfActive(ID id) {
        if (isUsed(id))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.ACTIVE));
    }

    protected void throwIfNotExists(ID id) {
        if (!getRepository().existsById(id)) 
            throw new EntityNotFoundException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.NOT_FOUND));
    }
}
