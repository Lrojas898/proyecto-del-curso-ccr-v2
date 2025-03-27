package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.repository.UserRoleRepository;
import com.example.ccrHospitalManagement.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;


import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
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
    void testCreateUser_WhenRoleIdsIsNull_ThrowsIllegalArgumentException() {
        // Datos de entrada
        List<String> roleIds = null;

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, roleIds));
        assertEquals("El usuario debe tener al menos un rol asignado.", exception.getMessage());
    }

    @Test
    void testCreateUser_WhenRoleIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Datos de entrada
        List<String> roleIds = new ArrayList<>();

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, roleIds));
        assertEquals("El usuario debe tener al menos un rol asignado.", exception.getMessage());
    }

    @Test
    void testCreateUser_WhenRoleNotFound_ThrowsIllegalArgumentException() {
        // Datos de entrada
        List<String> roleIds = List.of("nonExistentRoleId");

        // Comportamiento esperado
        when(roleRepository.findById("nonExistentRoleId")).thenReturn(Optional.empty());

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, roleIds));
        assertEquals("El rol con id nonExistentRoleId no existe.", exception.getMessage());
    }

    @Test
    void testCreateUser_WhenValidRole_ReturnsCreatedUser() {
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
    void testUpdateUser_WhenRoleIdsIsNull_ThrowsIllegalArgumentException() {
        // Datos de entrada
        List<String> roleIds = null;

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, roleIds));
        assertEquals("El usuario debe tener al menos un rol asignado.", exception.getMessage());
    }

    @Test
    void testUpdateUser_WhenRoleIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Datos de entrada
        List<String> roleIds = new ArrayList<>();

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, roleIds));
        assertEquals("El usuario debe tener al menos un rol asignado.", exception.getMessage());
    }

    @Test
    void testUpdateUser_WhenRoleNotFound_ThrowsIllegalArgumentException() {
        // Datos de entrada
        List<String> roleIds = List.of("nonExistentRoleId");

        // Comportamiento esperado
        when(roleRepository.findById("nonExistentRoleId")).thenReturn(Optional.empty());

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, roleIds));
        assertEquals("El rol con id nonExistentRoleId no existe.", exception.getMessage());
    }

    @Test
    void testUpdateUser_WhenValidRole_ReturnsUpdatedUser() {
        // Datos de entrada
        List<String> roleIds = List.of("roleId1");
        user.setUsername("updatedUser");

        // Comportamiento esperado
        when(roleRepository.findById("roleId1")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Ejecución
        User updatedUser = userService.updateUser(user, roleIds);

        // Verificación
        assertNotNull(updatedUser);
        assertEquals("updatedUser", updatedUser.getUsername());
        verify(roleRepository, times(1)).findById("roleId1");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_WhenUserNotFound_ThrowsIllegalArgumentException() {
        // Simulamos que el usuario no se encuentra en la base de datos
        when(userRepository.findById("nonExistentUserId")).thenReturn(Optional.empty());

        // Ejecución y verificación
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser("nonExistentUserId"));
        assertEquals("Usuario no encontrado", exception.getMessage());

        // Verificación: no se debe llamar a deleteById si el usuario no existe
        verify(userRepository, never()).deleteById("nonExistentUserId");
    }

    @Test
    void testDeleteUser_WhenUserExists() {
        // Simulamos que el usuario existe en la base de datos
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));

        // Ejecución
        userService.deleteUser("userId1");

        // Verificación: El método deleteById debe ser llamado una vez
        verify(userRepository, times(1)).deleteById("userId1");
    }

    @Test
    void testGetUserById_WhenUserNotFound() {
        // Simulamos que el usuario no se encuentra en la base de datos
        when(userRepository.findById("nonExistentUserId")).thenReturn(Optional.empty());
    
        // Ejecución
        Optional<User> result = userService.getUserById("nonExistentUserId");
    
        // Verificación: no debe encontrarse el usuario
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById("nonExistentUserId");
    }
    
    @Test
    void testGetAllUsers_WhenNoUsers() {
        // Simulamos que no hay usuarios en la base de datos
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
    
        // Ejecución
        List<User> result = userService.getAllUsers();
    
        // Verificación: debe devolver una lista vacía
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
    
}
