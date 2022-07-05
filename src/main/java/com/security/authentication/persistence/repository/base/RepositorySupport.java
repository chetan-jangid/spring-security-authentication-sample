package com.security.authentication.persistence.repository.base;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public interface RepositorySupport {
    EntityManager entityManager();
}
