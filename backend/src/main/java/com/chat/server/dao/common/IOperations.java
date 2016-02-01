package com.chat.server.dao.common;

import java.io.Serializable;
import java.util.List;

public interface IOperations<T extends Serializable> {
    T findOne(final int id);

    List<T> findAll();

    void create(final T entity);

    T update(final T entity);

    void delete(final T entity);

    void deleteById(final int entityId);
}
