package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Permission;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.repository.PermissionRepository;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.service.RoleService;
import com.example.ccrHospitalManagement.service.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    //Cargamos una simulación de la capa repository
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    //Inyectamos el mock a RoleService teniendo en cuenta la dependencia que tiene
    @InjectMocks
    private RoleServiceImpl roleService;

    // insercion
    @Test
    void createRole_WhenCalled_ReturnsRole(){
// Arrange
        Role roleToCreate = new Role();
        roleToCreate.setId("ADMIN");
        roleToCreate.setName("admin");

        List<String> permissionIds = Arrays.asList("READ", "WRITE");

        // Mockear los permisos existentes
        Permission permRead = new Permission();
        permRead.setId("READ");
        permRead.setName("Read Permission");

        Permission permWrite = new Permission();
        permWrite.setId("WRITE");
        permWrite.setName("Write Permission");

        when(permissionRepository.findById("READ")).thenReturn(Optional.of(permRead));
        when(permissionRepository.findById("WRITE")).thenReturn(Optional.of(permWrite));

        when(roleRepository.save(roleToCreate)).thenReturn(roleToCreate);

        // Act
        Role result = roleService.createRole(roleToCreate, permissionIds);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getId());
        assertEquals("admin", result.getName());
        verify(permissionRepository, times(1)).findById("READ");
        verify(permissionRepository, times(1)).findById("WRITE");
        verify(roleRepository, times(1)).save(roleToCreate);
    }
    @Test
    void createRole_WhenPermissionIdsIsNull_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToCreate = new Role();
        roleToCreate.setId("ROLE_ADMIN");
        roleToCreate.setName("Admin");

        //act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(roleToCreate,
                        null)
        );
        assertEquals("El rol debe tener al menos un permiso asignado.", e.getMessage());
        verify(roleRepository, never()).save(roleToCreate);
    }
    @Test
    void createRole_WhenPermissionIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToCreate = new Role();
        roleToCreate.setId("ROLE_ADMIN");
        roleToCreate.setName("Admin");
        List<String> emptyPermissionIds = Arrays.asList();

        //act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(roleToCreate, emptyPermissionIds)
        );
        assertEquals("El rol debe tener al menos un permiso asignado.", e.getMessage());
        verify(roleRepository, never()).save(roleToCreate);
    }

    @Test
    void createRole_WhenPermissionDoesNotExist_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToCreate = new Role();
        roleToCreate.setId("ROLE_TEST");
        roleToCreate.setName("Test Role");

        List<String> permissionIds = Arrays.asList("PERM_READ", "PERM_NONEXISTENT");

        Permission permRead = new Permission("PERM_READ", "Read", "Read access");
        when(permissionRepository.findById("PERM_READ")).thenReturn(Optional.of(permRead));
        when(permissionRepository.findById("PERM_NONEXISTENT")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(roleToCreate, permissionIds)
        );

        // Assert
        assertEquals("Permiso con ID PERM_NONEXISTENT no existe", exception.getMessage());
        verify(permissionRepository, times(1)).findById("PERM_READ");
        verify(permissionRepository, times(1)).findById("PERM_NONEXISTENT");
        verify(roleRepository, never()).save(any(Role.class));
    }

    // consulta
    @Test
    void getAllRoles_WhenCalled_ReturnsAllRoles(){
        //arrange
        Role role1 = new Role();
        role1.setId("ROLE_ADMIN");
        role1.setName("Admin");

        Role role2 = new Role();
        role2.setId("ROLE_USER");
        role2.setName("User");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        List<Role> result = roleService.getAllRoles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ROLE_ADMIN", result.get(0).getId());
        assertEquals("ROLE_USER", result.get(1).getId());
        verify(roleRepository, times(1)).findAll();
    }
    // consulta by id
    @Test
    void getRoleById_WhenRoleExists_ReturnsRole(){
        //arrange
        String roleId = "ROLE_ADMIN";
        Role role = new Role();
        role.setId(roleId);
        role.setName("Admin");

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        Optional<Role> result = roleService.getRoleById(roleId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("ROLE_ADMIN", result.get().getId());
        assertEquals("Admin", result.get().getName());
        verify(roleRepository, times(1)).findById(roleId);
    }
    @Test
    void getRoleById_WhenRoleDoesNotExist_ReturnsEmptyOptional(){
        // Arrange
        String roleId = "ROLE_NONEXISTENT";
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act
        Optional<Role> result = roleService.getRoleById(roleId);

        // Assert
        assertFalse(result.isPresent());
        verify(roleRepository, times(1)).findById(roleId);
    }
    //actualizacion
    @Test
    void updateRole_WhenValidInput_ReturnsUpdatedRole(){
        // Arrange
        Role roleToUpdate = new Role();
        roleToUpdate.setId("ROLE_ADMIN");
        roleToUpdate.setName("Updated Admin");

        List<String> permissionIds = Arrays.asList("PERM_READ", "PERM_UPDATE");

        // Mockear los permisos existentes
        Permission permRead = new Permission();
        permRead.setId("PERM_READ");
        permRead.setName("Read Permission");

        Permission permUpdate = new Permission();
        permUpdate.setId("PERM_UPDATE");
        permUpdate.setName("Update Permission");

        when(permissionRepository.findById("PERM_READ")).thenReturn(Optional.of(permRead));
        when(permissionRepository.findById("PERM_UPDATE")).thenReturn(Optional.of(permUpdate));

        // Mockear el guardado del rol
        when(roleRepository.save(roleToUpdate)).thenReturn(roleToUpdate);

        // Act
        Role result = roleService.updateRole(roleToUpdate, permissionIds);

        // Assert
        assertNotNull(result);
        assertEquals("ROLE_ADMIN", result.getId());
        assertEquals("Updated Admin", result.getName());
        verify(permissionRepository, times(1)).findById("PERM_READ");
        verify(permissionRepository, times(1)).findById("PERM_UPDATE");
        verify(roleRepository, times(1)).save(roleToUpdate);
    }
    @Test
    void updateRole_WhenPermissionIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToUpdate = new Role();
        roleToUpdate.setId("ROLE_ADMIN");
        List<String> emptyPermissionIds = Arrays.asList();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.updateRole(roleToUpdate, emptyPermissionIds)
        );
        assertEquals("El rol debe tener al menos un permiso asignado.", exception.getMessage());
        verify(roleRepository, never()).save(any(Role.class));
        verify(permissionRepository, never()).findById(anyString()); // Aseguramos que no se consulta PermissionRepository
    }


    @Test
    void updateRole_WhenPermissionIdsIsNull_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToUpdate = new Role();
        roleToUpdate.setId("ROLE_ADMIN");
        roleToUpdate.setName("Admin");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.updateRole(roleToUpdate, null)
        );

        // Assert
        assertEquals("El rol debe tener al menos un permiso asignado.", exception.getMessage());
        verify(permissionRepository, never()).findById(anyString());
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void updateRole_WhenPermissionIdDoesNotExist_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToUpdate = new Role();
        roleToUpdate.setId("ROLE_ADMIN");
        roleToUpdate.setName("Admin");

        List<String> permissionIds = Arrays.asList("PERM_READ", "PERM_NONEXISTENT");

        Permission permRead = new Permission("PERM_READ", "Read", "Read access");
        when(permissionRepository.findById("PERM_READ")).thenReturn(Optional.of(permRead));
        when(permissionRepository.findById("PERM_NONEXISTENT")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.updateRole(roleToUpdate, permissionIds)
        );

        // Assert
        assertEquals("Permiso con ID PERM_NONEXISTENT no existe", exception.getMessage());
        verify(permissionRepository, times(1)).findById("PERM_READ");
        verify(permissionRepository, times(1)).findById("PERM_NONEXISTENT");
        verify(roleRepository, never()).save(any(Role.class));
    }

    // borrado
    @Test
    void deleteRole_WhenCalled_DeletesRole() {
        // Arrange
        String roleId = "ROLE_ADMIN";
        doNothing().when(roleRepository).deleteById(roleId);

        // Act
        roleService.deleteRole(roleId);

        // Assert
        verify(roleRepository, times(1)).deleteById(roleId);
        verify(permissionRepository, never()).findById(anyString()); // Aseguramos que no se consulta PermissionRepository
    }
}
