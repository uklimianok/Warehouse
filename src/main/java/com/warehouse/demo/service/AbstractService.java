package com.warehouse.demo.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.util.action.MessageHandler;
import com.warehouse.demo.util.exception.BusinessRuleException;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;

public abstract class AbstractService<T, ID> {
    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract Entity getEntityName();

    public List<T> readAll() {
        return getRepository().findAll();
    }

    public T read(ID id) {
        throwIfNotExists(id);
        return getRepository().findById(id).get();
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
