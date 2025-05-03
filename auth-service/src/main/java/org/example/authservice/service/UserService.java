package org.example.authservice.service;

import org.example.authservice.dto.SignUpRequest;
import org.example.authservice.dto.UserListResponse;
import org.example.authservice.dto.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse getUser(String name);

    UserListResponse getAllUsers(Pageable pageable);

    UserResponse createUser(SignUpRequest signUpRequest);

    void deleteUser(String name);
}
