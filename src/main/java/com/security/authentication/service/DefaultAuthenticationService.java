package com.security.authentication.service;

import com.security.authentication.api.dto.AuthenticationRequest;
import com.security.authentication.api.dto.AuthenticationResponse;
import com.security.authentication.persistence.entity.UserAccount;
import com.security.authentication.persistence.repository.jpa.UserAccountJpaRepository;
import com.security.authentication.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {
    private final UserAccountJpaRepository userAccountJpaRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Objects.requireNonNull(request, () -> {
            throw new BadCredentialsException("Invalid request.");
        });
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User principal = (User) authentication.getPrincipal();
        return getToken(principal.getUsername());
    }

    @Override
    public void logout(String token) {
        Objects.requireNonNull(token, () -> {
            throw new BadCredentialsException("Invalid token found.");
        });
        String username = TokenUtils.getUsername(token);
        UserAccount userAccount = this.userAccountJpaRepository.findByUsername(username);
        userAccount.setTokenIdentifier(null);
        this.userAccountJpaRepository.save(userAccount);
    }

    private AuthenticationResponse getToken(String username) {
        UserAccount userAccount = this.userAccountJpaRepository.findByUsername(username);
        Objects.requireNonNull(userAccount, () -> {
            throw new UsernameNotFoundException("User not found.");
        });
        String token = TokenUtils.generateToken(username);
        userAccount.setTokenIdentifier(token);
        this.userAccountJpaRepository.save(userAccount);
        return new AuthenticationResponse(token);
    }
}
