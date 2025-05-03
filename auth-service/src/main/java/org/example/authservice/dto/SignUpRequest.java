package org.example.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(@NotBlank(message = "Name must not be blank") String name,
                            @Email(message = "Email required") String email,
                            @Size(min = 5, max = 64, message = "Password must be between 5 and 64 characters") String password) {
}
