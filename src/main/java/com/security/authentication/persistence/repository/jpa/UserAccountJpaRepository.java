package com.security.authentication.persistence.repository.jpa;

import com.security.authentication.persistence.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountJpaRepository extends JpaRepository<UserAccount, Long> {
    @Query("select u from UserAccount u where u.username = ?1")
    UserAccount findByUsername(String username);
}
