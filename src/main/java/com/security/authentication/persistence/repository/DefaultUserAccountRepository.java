package com.security.authentication.persistence.repository;

import com.security.authentication.persistence.repository.base.SimpleRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DefaultUserAccountRepository extends SimpleRepositorySupport implements UserAccountRepository {
    @Override
    public List<String> getRolesByUserAccountId(Long userAccountId) {
        return this.entityManager()
                .createNamedQuery("UserAccountRole.getRolesByUserAccountId", String.class)
                .setParameter("userAccountId", userAccountId)
                .getResultList();
    }
}
