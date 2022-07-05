package com.security.authentication.persistence.repository;

import java.util.List;

public interface UserAccountRepository {
    List<String> getRolesByUserAccountId(Long userAccountId);
}
