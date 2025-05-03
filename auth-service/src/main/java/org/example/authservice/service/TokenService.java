package org.example.authservice.service;

import org.example.authservice.domain.RefreshToken;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.UpdateTokensResponse;

public interface TokenService {

    AuthResponse login(LoginRequest request);

    void logout();

    RefreshToken createRefreshToken(Long userId);

    UpdateTokensResponse updateTokens(String refreshToken);
}
