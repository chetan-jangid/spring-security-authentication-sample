package com.security.authentication.persistence.repository.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class SimpleRepositorySupport implements RepositorySupport {
    @PersistenceContext
    private EntityManager em;

    @Override
    public EntityManager entityManager() {
        return this.em;
    }
}
