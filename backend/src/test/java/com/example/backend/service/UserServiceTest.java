package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUser() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("pass123");

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);


        User registered = userService.registerUser("testuser", "testuser@example.com", "pass123");


        assertThat(registered.getEmail()).isEqualTo("testuser@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception if email already exists")
    void testRegisterUserDuplicateEmail() {

        User existing = new User();
        existing.setEmail("testuser@example.com");

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(existing));


        assertThatThrownBy(() -> userService.registerUser("user", "testuser@example.com", "pass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindUserByEmail() {

        User user = new User();
        user.setEmail("findme@example.com");

        when(userRepository.findByEmail("findme@example.com")).thenReturn(Optional.of(user));


        Optional<User> found = userService.findByEmail("findme@example.com");


        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("findme@example.com");
    }
}
