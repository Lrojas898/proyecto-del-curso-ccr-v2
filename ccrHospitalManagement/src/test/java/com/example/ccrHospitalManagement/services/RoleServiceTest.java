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
        role.setId("ROLE_ADMIN");
        role.setName("Admin");
    }

    @Test
    void createRole_WhenValid_ReturnsRole() {
        when(roleRepository.existsById(role.getId())).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.createRole(role);

        assertNotNull(result);
        assertEquals("ROLE_ADMIN", result.getId());
        assertEquals("Admin", result.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void createRole_WhenIdExists_ThrowsException() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));

        assertEquals("Ya existe un rol con ese ID.", e.getMessage());
        verify(roleRepository, never()).save(role);
    }

    @Test
    void createRole_WhenNameInvalid_ThrowsException() {
        role.setName("Ad");
        when(roleRepository.existsById(role.getId())).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));

        assertEquals("El nombre del rol debe tener al menos 3 caracteres.", e.getMessage());
    }

    @Test
    void createRole_WhenIdIsEmpty_ThrowsException() {
        role.setId("");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));
        assertEquals("El ID del rol es obligatorio.", e.getMessage());
    }

    @Test
    void getAllRoles_WhenCalled_ReturnsAllRoles() {
        Role role1 = new Role();
        role1.setId("ROLE_ADMIN");
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId("ROLE_USER");
        role2.setName("User");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void getRoleById_WhenExists_ReturnsRole() {
        when(roleRepository.findById("ROLE_ADMIN")).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.getRoleById("ROLE_ADMIN");

        assertTrue(result.isPresent());
        assertEquals("Admin", result.get().getName());
    }

    @Test
    void getRoleById_WhenNotExists_ReturnsEmpty() {
        when(roleRepository.findById("ROLE_UNKNOWN")).thenReturn(Optional.empty());

        Optional<Role> result = roleService.getRoleById("ROLE_UNKNOWN");

        assertFalse(result.isPresent());
    }

    @Test
    void updateRole_WhenValid_ReturnsUpdated() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.updateRole(role);

        assertEquals("ROLE_ADMIN", result.getId());
        verify(roleRepository).save(role);
    }

    @Test
    void updateRole_WhenNotExists_ThrowsException() {
        when(roleRepository.existsById(role.getId())).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.updateRole(role));

        assertEquals("El rol con ID ROLE_ADMIN no existe.", e.getMessage());
        verify(roleRepository, never()).save(role);
    }

    @Test
    void updateRole_WhenNameInvalid_ThrowsException() {
        role.setName("A");
        when(roleRepository.existsById(role.getId())).thenReturn(true);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.updateRole(role));

        assertEquals("El nombre del rol debe tener al menos 3 caracteres.", e.getMessage());
    }

    @Test
    void deleteRole_WhenExists_DeletesRole() {
        when(roleRepository.existsById("ROLE_ADMIN")).thenReturn(true);

        roleService.deleteRole("ROLE_ADMIN");

        verify(roleRepository).deleteById("ROLE_ADMIN");
    }

    @Test
    void deleteRole_WhenNotExists_ThrowsException() {
        when(roleRepository.existsById("ROLE_UNKNOWN")).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.deleteRole("ROLE_UNKNOWN"));

        assertEquals("El rol con ID ROLE_UNKNOWN no existe.", e.getMessage());
    }
}