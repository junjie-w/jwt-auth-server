package com.example.jwt_auth_server.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jwt_auth_server.model.Role;
import com.example.jwt_auth_server.model.User;
import com.example.jwt_auth_server.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User("testuser", "test@example.com", "password");
        testUser.addRole(Role.ROLE_USER);
        
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    public void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        
        UserDetails userDetails = userService.loadUserByUsername("testuser");
        
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void loadUserByUsername_NonExistingUser_ThrowsException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("nonexistent");
        });
        assertNotNull(exception);
    }

    @Test
    public void createUser_ValidUser_ReturnsCreatedUser() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        User createdUser = userService.createUser(testUser);
        
        assertNotNull(createdUser);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(testUser);
    }

    @Test
    public void createUser_ExistingUsername_ThrowsException() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(testUser);
        });
        assertNotNull(exception);
    }
}
