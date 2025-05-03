package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.domain.RefreshToken;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.UpdateTokensResponse;
import org.example.authservice.exception.RefreshTokenException;
import org.example.authservice.exception.UserNotFoundException;
import org.example.authservice.repository.RefreshTokenRepository;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.security.AppUserDetails;
import org.example.authservice.security.JwtUtils;
import org.example.authservice.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.name(),
                request.password()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails principal = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return AuthResponse.builder()
                .username(principal.getUsername())
                .roles(roles)
                .accessToken(jwtUtils.generateToken(principal))
                .refreshToken(createRefreshToken(principal.getId()).getToken())
                .build();
    }

    @Override
    public void logout() {
        AppUserDetails principal = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenRepository.deleteByUserId(principal.getId());
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(userId)
                .build();
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public UpdateTokensResponse updateTokens(String refreshToken) {
        RefreshToken currentRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
        Long userId =currentRefreshToken.getUserId();
        String newAccessToken = jwtUtils.generateTokenByUsername(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(MessageFormat.format("User with id {0} not found", userId)))
                .getName()
        );
        String newRefreshToken = createRefreshToken(userId).getToken();
        refreshTokenRepository.delete(currentRefreshToken);

        return new UpdateTokensResponse(newAccessToken, newRefreshToken);
    }
}
