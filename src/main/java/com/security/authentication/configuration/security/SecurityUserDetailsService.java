package com.security.authentication.configuration.security;

import com.security.authentication.persistence.entity.UserAccount;
import com.security.authentication.persistence.repository.UserAccountRepository;
import com.security.authentication.persistence.repository.jpa.UserAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserAccountJpaRepository userAccountJpaRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = this.userAccountJpaRepository.findByUsername(username);
        verifyUserAccount(userAccount);
        return getUserDetails(userAccount);
    }

    public UserAccount userAccount(String username) {
        return this.userAccountJpaRepository.findByUsername(username);
    }

    private User getUserDetails(UserAccount userAccount) {
        List<String> roles = this.userAccountRepository.getRolesByUserAccountId(userAccount.getId());
        return new User(userAccount.getUsername(), userAccount.getPasswordHash(),
                roles.stream().map(SimpleGrantedAuthority::new).toList());
    }

    private void verifyUserAccount(UserAccount userAccount) {
        Objects.requireNonNull(userAccount, () -> {
            throw new UsernameNotFoundException("No such user present.");
        });
        if (Boolean.FALSE.equals(userAccount.getIsActive())) {
            throw new UsernameNotFoundException("Account is deactivated.");
        }
    }
}
