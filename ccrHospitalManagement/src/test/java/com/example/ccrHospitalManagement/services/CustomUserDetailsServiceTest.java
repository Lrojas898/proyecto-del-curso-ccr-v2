package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.CustomUserDetails;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.service.CustomUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("U123");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setRoles(Set.of(new Role("ROLE_USER", "User")));
    }

    @Test
    void loadUserByUsername_ValidUser_ReturnsUserDetails() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = service.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails instanceof CustomUserDetails);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown"));

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void loadUserByUsername_EmptyUsername_ReturnsNullUser_ThrowsException() {
        when(userRepository.findByUsername("")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(""));

        assertEquals("Usuario no encontrado: ", exception.getMessage());
    }

    @Test
    void loadUserByUsername_NullUsername_ThrowsException() {
        when(userRepository.findByUsername(null)).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername(null));

        assertEquals("Usuario no encontrado: null", exception.getMessage());
    }
} 

