package com.chat.server.service.common;

import com.chat.server.dao.common.IOperations;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 28.10.2015.
 */
public abstract class AbstractService<T extends Serializable> implements IOperations<T>{
    @Override
    @Transactional
    public T findOne(final int id) {
        return getDao().findOne(id);
    }

    @Override
    @Transactional
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    @Transactional
    public void create(final T entity) {
        getDao().create(entity);
    }

    @Override
    @Transactional
    public T update(final T entity) {
        return getDao().update(entity);
    }

    @Override
    @Transactional
    public void delete(final T entity) {
        getDao().delete(entity);
    }

    @Override
    @Transactional
    public void deleteById(final int entityId) {
        getDao().deleteById(entityId);
    }

    protected abstract IOperations<T> getDao();
}
