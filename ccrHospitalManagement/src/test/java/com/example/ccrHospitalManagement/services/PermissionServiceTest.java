package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Permission;
import com.example.ccrHospitalManagement.repository.PermissionRepository;
import com.example.ccrHospitalManagement.service.PermissionService;
import com.example.ccrHospitalManagement.service.PermissionServiceImpl;
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
public class PermissionServiceTest {
    //Cargamos una simulación de la capa repository
    @Mock
    private PermissionRepository permissionRepository;
    //Inyectamos el mock a PermissionService teniendo en cuenta la dependencia que tiene
    @InjectMocks
    private PermissionServiceImpl permissionService;
    // Test para inserción (createPermission)
    @Test
    void createPermission_WhenCalled_ReturnsSavedPermission() {
        // Arrange
        Permission permissionToCreate = new Permission();
        permissionToCreate.setId("PERM_READ");
        permissionToCreate.setName("Read Permission");

        when(permissionRepository.save(permissionToCreate)).thenReturn(permissionToCreate);

        // Act
        Permission result = permissionService.createPermission(permissionToCreate);

        // Assert
        assertNotNull(result);
        assertEquals("PERM_READ", result.getId());
        assertEquals("Read Permission", result.getName());
        verify(permissionRepository, times(1)).save(permissionToCreate);
    }

    // Test para consulta (getAllPermissions)
    @Test
    void getAllPermissions_WhenCalled_ReturnsAllPermissions() {
        // Arrange
        Permission perm1 = new Permission();
        perm1.setId("PERM_READ");
        perm1.setName("Read Permission");

        Permission perm2 = new Permission();
        perm2.setId("PERM_WRITE");
        perm2.setName("Write Permission");

        List<Permission> permissions = Arrays.asList(perm1, perm2);
        when(permissionRepository.findAll()).thenReturn(permissions);

        // Act
        List<Permission> result = permissionService.getAllPermissions();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("PERM_READ", result.get(0).getId());
        assertEquals("PERM_WRITE", result.get(1).getId());
        verify(permissionRepository, times(1)).findAll();
    }

    // Test para consulta (getPermissionById)
    @Test
    void getPermissionById_WhenPermissionExists_ReturnsPermission() {
        // Arrange
        String permId = "PERM_READ";
        Permission permission = new Permission();
        permission.setId(permId);
        permission.setName("Read Permission");

        when(permissionRepository.findById(permId)).thenReturn(Optional.of(permission));

        // Act
        Optional<Permission> result = permissionService.getPermissionById(permId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("PERM_READ", result.get().getId());
        assertEquals("Read Permission", result.get().getName());
        verify(permissionRepository, times(1)).findById(permId);
    }

    @Test
    void getPermissionById_WhenPermissionDoesNotExist_ReturnsEmptyOptional() {
        // Arrange
        String permId = "PERM_NONEXISTENT";
        when(permissionRepository.findById(permId)).thenReturn(Optional.empty());

        // Act
        Optional<Permission> result = permissionService.getPermissionById(permId);

        // Assert
        assertFalse(result.isPresent());
        verify(permissionRepository, times(1)).findById(permId);
    }

    // Test para actualización (updatePermission)
    @Test
    void updatePermission_WhenCalled_ReturnsUpdatedPermission() {
        // Arrange
        Permission permissionToUpdate = new Permission();
        permissionToUpdate.setId("PERM_READ");
        permissionToUpdate.setName("Updated Read Permission");

        when(permissionRepository.save(permissionToUpdate)).thenReturn(permissionToUpdate);

        // Act
        Permission result = permissionService.updatePermission(permissionToUpdate);

        // Assert
        assertNotNull(result);
        assertEquals("PERM_READ", result.getId());
        assertEquals("Updated Read Permission", result.getName());
        verify(permissionRepository, times(1)).save(permissionToUpdate);
    }

    // Test para eliminación (deletePermission)
    @Test
    void deletePermission_WhenCalled_DeletesPermission() {
        // Arrange
        String permId = "PERM_READ";
        doNothing().when(permissionRepository).deleteById(permId);

        // Act
        permissionService.deletePermission(permId);

        // Assert
        verify(permissionRepository, times(1)).deleteById(permId);
    }

    // Test adicional para eliminación cuando el permiso no existe (opcional)
    @Test
    void deletePermission_WhenPermissionDoesNotExist_ThrowsException() {
        // Arrange
        String permId = "PERM_NONEXISTENT";
        doThrow(new org.springframework.dao.EmptyResultDataAccessException(1))
                .when(permissionRepository).deleteById(permId);

        // Act & Assert
        assertThrows(org.springframework.dao.EmptyResultDataAccessException.class,
                () -> permissionService.deletePermission(permId));
        verify(permissionRepository, times(1)).deleteById(permId);
    }
}
