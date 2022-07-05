package com.security.authentication;

import com.security.authentication.persistence.entity.AccountRole;
import com.security.authentication.persistence.entity.UserAccount;
import com.security.authentication.persistence.entity.UserAccountRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class DataInitializationRunner implements CommandLineRunner {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        saveSuperAdminRole();
        createAdminUser();
    }

    @Transactional
    public void createAdminUser() {
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("flash");
        userAccount.setPasswordHash("$2a$10$Dv8XfQVzVjLI21evwgq5we3I3co9rZMHRSTEauKs2PqgvbveE9Ztu");
        userAccount.setIsActive(true);
        UserAccountRole userAccountRole = new UserAccountRole();
        userAccountRole.setUserAccount(userAccount);
        userAccountRole.setRoleKey("super_admin");
        userAccount.setUserAccountRoles(List.of(userAccountRole));
        this.entityManager.persist(userAccount);
        this.entityManager.persist(userAccountRole);
    }

    @Transactional
    public void saveSuperAdminRole() {
        AccountRole accountRole = new AccountRole();
        accountRole.setRoleKey("super_admin");
        accountRole.setRoleName("Super Admin");
        accountRole.setRoleDescription("Global access");
        this.entityManager.persist(accountRole);
    }
}
