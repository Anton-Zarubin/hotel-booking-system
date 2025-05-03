package org.example.authservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AuthResponse(String username, List<String> roles, String accessToken, String refreshToken) {
}
