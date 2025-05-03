package org.example.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.LogoutResponse;
import org.example.authservice.dto.UpdateTokensResponse;
import org.example.authservice.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(tokenService.login(request));
    }

    @Operation(summary = "Get new pair of tokens", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/token/update")
    public ResponseEntity<UpdateTokensResponse> updateTokens(@RequestParam String refreshToken) {
        return ResponseEntity.ok(tokenService.updateTokens(refreshToken));
    }

    @Operation(summary = "Logout", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        tokenService.logout();
        return ResponseEntity.ok(new LogoutResponse(MessageFormat.format("User {0} logged out.",
                userDetails.getUsername())));
    }
}
