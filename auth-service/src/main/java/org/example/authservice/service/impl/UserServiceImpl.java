package org.example.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.authservice.domain.User;
import org.example.authservice.dto.SignUpRequest;
import org.example.authservice.dto.UserListResponse;
import org.example.authservice.dto.UserResponse;
import org.example.authservice.exception.UserNotFoundException;
import org.example.authservice.mapper.UserMapper;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public UserResponse getUser(String name) {
        return mapper.userToResponse(userRepository.findByName(name).orElseThrow(() ->
                new UserNotFoundException(MessageFormat.format("User with name {0} not found", name))));
    }

    @Override
    public UserListResponse getAllUsers(Pageable pageable) {
        return mapper.userListToUserListResponse(userRepository.findAll(pageable));
    }

    @Override
    public UserResponse createUser(SignUpRequest signUpRequest) {
        return mapper.userToResponse(
                userRepository.save(new User(
                        signUpRequest.name(),
                        signUpRequest.email(),
                        passwordEncoder.encode(signUpRequest.password()),
                        Collections.singletonList(roleRepository.findByName("ROLE_USER"))))
        );
    }

    @Transactional
    @Override
    public void deleteUser(String name) {
        userRepository.deleteByName(name);
    }
}
