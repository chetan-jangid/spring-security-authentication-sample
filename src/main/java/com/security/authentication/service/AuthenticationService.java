package com.security.authentication.service;

import com.security.authentication.api.dto.AuthenticationRequest;
import com.security.authentication.api.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    void logout(String token);
}
