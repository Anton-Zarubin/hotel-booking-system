package org.example.authservice.dto;

import java.util.List;

public record UserListResponse(Long total, List<UserResponse> users) {
}
