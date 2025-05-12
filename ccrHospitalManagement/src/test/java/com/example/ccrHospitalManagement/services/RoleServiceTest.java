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
import static org.mockito.ArgumentMatchers.any;
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
        when(roleRepository.existsById(role.getId())).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.createRole(role);

        assertNotNull(result);
        assertEquals(1L, result.getId());
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
    void createRole_WhenNameIsNull_ThrowsException() {
        role.setName(null);
        when(roleRepository.existsById(1L)).thenReturn(false);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.createRole(role));
        assertEquals("El nombre del rol debe tener al menos 3 caracteres.", e.getMessage());
    }
    
    //Update

    @Test
    void updateRole_WhenValid_ReturnsUpdated() {
        when(roleRepository.existsById(role.getId())).thenReturn(true);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.updateRole(role);

        assertEquals(1L, result.getId());
        verify(roleRepository).save(role);
    }

    @Test
    void updateRole_WhenNotExists_ThrowsException() {
        when(roleRepository.existsById(role.getId())).thenReturn(false);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> roleService.updateRole(role));

        assertEquals("El rol con ID 1 no existe.", e.getMessage());
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

    //remove

    @Test
    void deleteRole_WhenExists_DeletesRole() {
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

    //get
    
    @Test
    void getAllRoles_WhenCalled_ReturnsAllRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId(2L);
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