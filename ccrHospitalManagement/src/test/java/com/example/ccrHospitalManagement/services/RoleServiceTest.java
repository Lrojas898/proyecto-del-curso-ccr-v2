package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.service.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("Admin");
    }

    // CREATE

    @Test
    void createRole_WhenValid_ReturnsRole() {
        role.setId(null); // El ID debe ser null para crear
        when(roleRepository.findByName("Admin")).thenReturn(Optional.empty());
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.createRole(role);

        assertNotNull(result);
        assertEquals("Admin", result.getName());
        verify(roleRepository).save(role);
    }

    @Test
    void createRole_WhenIdIsNotNull_ThrowsException() {
        role.setId(1L); // debe lanzar error porque no debe tener ID
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));
        assertEquals("No se debe proporcionar un ID al crear un rol.", e.getMessage());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void createRole_WhenNameIsNull_ThrowsException() {
        role.setId(null);
        role.setName(null);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));
        assertEquals("El nombre del rol es obligatorio.", e.getMessage());
    }

    @Test
    void createRole_WhenNameAlreadyExists_ThrowsException() {
        role.setId(null);
        when(roleRepository.findByName("Admin")).thenReturn(Optional.of(role));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));

        assertEquals("Ya existe un rol con ese nombre.", e.getMessage());
    }

    // UPDATE

    @Test
    void updateRole_WhenValid_ReturnsUpdatedRole() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);
        when(roleRepository.save(role)).thenReturn(role);

        Role updated = roleService.updateRole(role);

        assertEquals("Admin", updated.getName());
        verify(roleRepository).save(role);
    }

    @Test
    void updateRole_WhenNotExists_ThrowsException() {
        when(roleRepository.existsById(role.getId())).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.updateRole(role));

        assertEquals("El rol con ID 1 no existe.", e.getMessage());
    }

    @Test
    void updateRole_WhenNameTooShort_ThrowsException() {
        role.setName("A");
        when(roleRepository.existsById(role.getId())).thenReturn(true);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.updateRole(role));

        assertEquals("El nombre del rol debe tener al menos 3 caracteres.", e.getMessage());
    }

    // DELETE

    @Test
    void deleteRole_WhenExists_DeletesSuccessfully() {
        when(roleRepository.existsById(1L)).thenReturn(true);

        roleService.deleteRole(1L);

        verify(roleRepository).deleteById(1L);
    }

    @Test
    void deleteRole_WhenNotExists_ThrowsException() {
        when(roleRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.deleteRole(99L));

        assertEquals("El rol con ID 99 no existe.", e.getMessage());
    }

    // GET

    @Test
    void getAllRoles_ReturnsList() {
        List<Role> roles = Arrays.asList(
                new Role(1L, "Admin"),
                new Role(2L, "User")
        );

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertEquals(2, result.size());
        verify(roleRepository).findAll();
    }

    @Test
    void getRoleById_WhenExists_ReturnsRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.getRoleById(1L);

        assertTrue(result.isPresent());
        assertEquals("Admin", result.get().getName());
    }

    @Test
    void getRoleById_WhenNotExists_ReturnsEmpty() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Role> result = roleService.getRoleById(99L);

        assertFalse(result.isPresent());
    }
}
