package com.security.authentication.persistence.repository.base;

import com.security.authentication.persistence.entity.BaseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SimpleJpaCrudRepository<T extends BaseEntity, I extends Serializable>
        extends SimpleRepositorySupport implements JpaCrudRepository<T, I>, RepositorySupport {
    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        this.entityManager().persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(this.save(entity));
        }
        return result;
    }

    @Override
    @Transactional
    public void delete(T entity) {
        this.entityManager().remove(entity);
    }

    @Override
    public Optional<T> findById(Class<T> tClass, I id) {
        return Optional.ofNullable(this.entityManager().find(tClass, id));
    }

    @Override
    @Transactional
    public <S extends T> S merge(S entity) {
        return this.entityManager().merge(entity);
    }
}
