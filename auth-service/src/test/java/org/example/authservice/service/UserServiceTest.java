package org.example.authservice.service;

import org.example.authservice.domain.Role;
import org.example.authservice.domain.User;
import org.example.authservice.dto.SignUpRequest;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    private User newUser;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0) + "_some_fake_encoding");
        user = new User(
                "Petrov",
                "petrov@test.tst",
                passwordEncoder.encode("password"),
                Collections.singletonList(new Role("ROLE_USER"))
        );
        newUser = new User(
                "Ivanov",
                "ivanov@test.tst",
                passwordEncoder.encode("password"),
                Collections.singletonList(new Role("ROLE_USER"))
        );
        users = Collections.singletonList(user);
    }

    @Test
    void whenUserExists_thenReturnUser() {
        when(userRepository.findByName("Petrov")).thenReturn(Optional.ofNullable(user));
        assertDoesNotThrow(() -> userService.getUser("Petrov"));
    }

    @Test
    void whenUserNotFound_thenException() {
        when(userRepository.findByName("Sidorov")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUser("Sidorov"));
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll(Pageable.ofSize(1))).thenReturn(new PageImpl<>(users));
        assertDoesNotThrow(() -> userService.getAllUsers(PageRequest.of(0, 1)));
    }

    @Test
    void createUser() {
        SignUpRequest signUpRequest = new SignUpRequest("Ivanov", "ivanov@test.tst", "password");

        when(userRepository.save(any(User.class))).thenReturn(newUser);
        assertDoesNotThrow(() -> userService.createUser(signUpRequest));
    }
}
