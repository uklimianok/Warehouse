package com.warehouse.demo.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

public abstract class AbstractCrudService<T, ID> {
    protected abstract JpaRepository<T, ID> getRepository();
    protected abstract String getEntityName();

    private void throwIfNotExists(ID id) {
        if (!getRepository().existsById(id)) 
            throw new EntityNotFoundException(getEntityName() + " not found.");
    }

    private void throwIfActive(ID id) {
        if (isUsed(id))
            throw new DataIntegrityViolationException(getEntityName() + " is active.");
    }

    protected boolean isUsed(ID id) {
        return false;   // default return value
    }

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
}
