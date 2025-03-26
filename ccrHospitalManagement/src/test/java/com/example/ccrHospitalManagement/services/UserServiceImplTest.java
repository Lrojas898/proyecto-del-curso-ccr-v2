package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.service.UserServiceImpl;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicialización de objetos
        role = new Role();
        role.setId("roleId1");
        role.setName("Patient");

        user = new User();
        user.setId("userId1");
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
    }

    @Test
    void testCreateUser() {
        // Datos de entrada
        List<String> roleIds = List.of("roleId1");

        // Comportamiento esperado
        when(roleRepository.findById("roleId1")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Ejecución
        User createdUser = userService.createUser(user, roleIds);

        // Verificación
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals(1, createdUser.getRoles().size());
        verify(roleRepository, times(1)).findById("roleId1");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        // Datos de entrada
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        // Ejecución
        List<User> result = userService.getAllUsers();

        // Verificación
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    void testGetUserById() {
        // Datos de entrada
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));

        // Ejecución
        Optional<User> result = userService.getUserById("userId1");

        // Verificación
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testUpdateUser() {
        // Datos de entrada
        List<String> roleIds = List.of("roleId1");
        user.setUsername("updatedUser");

        when(roleRepository.findById("roleId1")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Ejecución
        User updatedUser = userService.updateUser(user, roleIds);

        // Verificación
        assertNotNull(updatedUser);
        assertEquals("updatedUser", updatedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        // Ejecución
        userService.deleteUser("userId1");

        // Verificación
        verify(userRepository, times(1)).deleteById("userId1");
    }
}
