package com.project.habit_tracker.Service;

import com.project.habit_tracker.Model.User;
import com.project.habit_tracker.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserserviceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserHashesPasswordBeforeSaving() {
        when(userRepository.findOptionalByUsername("jeremy")).thenReturn(Optional.empty());
        when(userRepository.findOptionalByEmail("jeremy@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("$2a$10$encryptedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(" jeremy ", " Jeremy Chua ", " Jeremy@example.com ", "plainPassword");

        ArgumentCaptor<User> savedUserCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(savedUserCaptor.capture());

        User persistedUser = savedUserCaptor.getValue();
        assertEquals("jeremy", persistedUser.getUsername());
        assertEquals("Jeremy Chua", persistedUser.getName());
        assertEquals("jeremy@example.com", persistedUser.getEmail());
        assertEquals("$2a$10$encryptedPassword", persistedUser.getPassword());
        assertEquals("$2a$10$encryptedPassword", savedUser.getPassword());
        verify(passwordEncoder).encode("plainPassword");
    }

    @Test
    void registerUserRejectsDuplicateUsername() {
        User existingUser = new User();
        existingUser.setUsername("jeremy");
        when(userRepository.findOptionalByUsername("jeremy")).thenReturn(Optional.of(existingUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser("jeremy", "Jeremy", "jeremy@example.com", "plainPassword"));

        assertTrue(exception.getMessage().contains("Username"));
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserRejectsDuplicateEmail() {
        User existingUser = new User();
        existingUser.setEmail("jeremy@example.com");
        when(userRepository.findOptionalByUsername("jeremy")).thenReturn(Optional.empty());
        when(userRepository.findOptionalByEmail("jeremy@example.com")).thenReturn(Optional.of(existingUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser("jeremy", "Jeremy", "jeremy@example.com", "plainPassword"));

        assertTrue(exception.getMessage().contains("Email"));
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }
}

