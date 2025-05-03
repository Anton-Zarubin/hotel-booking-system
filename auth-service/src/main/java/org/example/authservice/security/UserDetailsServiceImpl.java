package org.example.authservice.security;

import lombok.RequiredArgsConstructor;
import org.example.authservice.exception.UserNotFoundException;
import org.example.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UserNotFoundException {
        return new AppUserDetails(userRepository.findByName(name)
                .orElseThrow(() ->
                        new UserNotFoundException(MessageFormat.format("User with name {0} not found", name))));
    }
}
