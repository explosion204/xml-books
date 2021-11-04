package com.karnyshov.xmlbooks.exception;

public class EntityNotFoundException extends RuntimeException {
    private final Class<?> entityClass;

    public EntityNotFoundException(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
