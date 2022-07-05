package com.security.authentication.persistence.repository.base;

import com.security.authentication.persistence.entity.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface JpaCrudRepository<T extends BaseEntity, I extends Serializable> {
    @Transactional
    <S extends T> S save(S entity);

    @Transactional
    <S extends T> List<S> saveAll(Iterable<S> entities);

    @Transactional
    void delete(T entity);

    Optional<T> findById(Class<T> tClass, I id);

    @Transactional
    <S extends T> S merge(S entity);
}
